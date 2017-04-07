import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.list.ExpList;
import ast.list.FormalList;
import ast.list.StatementList;
import ast.list.VarDeclList;
import ast.statement.*;
import ast.type.*;
import com.sun.org.apache.xpath.internal.operations.Div;
import oldast.expression.IntLiteral;
import token.*;

import static token.TokenType.*;

public class Parser2 {

	private Lexer l;
	private Token tok;

	public Parser2(Lexer l) { this.l = l; tok = l.next(); }
	private void eat(TokenType... t) { for (int i = 0; i < t.length; i++) if (tok.type == t[i]) tok = l.next(); else error(); }
	private void error() { System.out.println(tok.col + ":" + tok.row + " error: ...");}

	Program program() { return new Program(main(), classDeclList()); }

	MainClass main() {
		eat(CLASS);
		Identifier i1 = identifier();
		eat(LBRACE, PUBLIC, STATIC, VOID, MAIN, LPAREN, STRING, LBRACKET, RBRACKET);
		Identifier i2 = identifier();
		eat(RPAREN, LBRACE);
		Statement s = statement();
		eat(RBRACE, RBRACE);
		return new MainClass(i1, i2, s);
	}

	ClassDecl classDecl() {
		eat(CLASS);
		Identifier i1 = identifier();
		eat(LPAREN);
		ClassDecl c = null;
		switch(tok.type) {
			case EXTENDS:
				Identifier i2 = identifier();
				eat(LBRACE);
				c = new ClassDeclExtends(i1, i2, varDeclList(), methodDeclList());
				break;
			case LBRACE:
				c = new ClassDeclSimple(i1, varDeclList(), methodDeclList());
				break;
			default: error();
		}
		eat(RBRACE);
		return c;
	}

	VarDecl varDecl() { return new VarDecl(type(), identifier()); }

	MethodDecl methodDecl() {
		eat(PUBLIC);
		Type t = type();
		Identifier i = identifier();
		eat(LPAREN);
		FormalList fl = formalList();
		eat(LBRACE);
		VarDeclList vl = varDeclList();
		StatementList sl = statementList();
		eat(RETURN);
		Exp e = exp();
		eat(SEMICOLON, RBRACE);
		return new MethodDecl(t, i, fl, vl, sl, e);
	}

	Formal formal() { return new Formal(type(), identifier()); }

	Type type() { switch (tok.type) {
			case BOOLEAN: eat(BOOLEAN); return new BooleanType();
			case STRING: eat(STRING); return new IdentifierType("String");
			case INT: eat(TokenType.INT);
				switch (tok.type) {
					case LBRACKET: eat(RBRACKET); return new IntArrayType();
					default: return new IntegerType();
				}
			case ID: return new IdentifierType(identifier().s);
			default: error(); return null;
	}}

	Block block() {
		eat(LBRACE);
		StatementList sl = statementList();
		eat(RBRACE);
		return new Block(sl);
	}

	If ifStmnt() {
		eat(IF, LPAREN);
		Exp e = exp();
		eat(RPAREN);
		Statement s1 = statement();
		if(tok.type != ELSE) return new If(e, s1, null);
		eat(ELSE); return new If(e, s1, statement());
	}

	While whileStmnt() {
		eat(WHILE, LPAREN);
		Exp e = exp();
		eat(RPAREN);
		return new While(e, statement());
	}

	Print print() {
		eat(PRINTLN, LPAREN);
		Exp e = exp();
		eat(RPAREN, SEMICOLON);
		return new Print(e);
	}

	Assign assign() {
		Identifier i = identifier();
		eat(EQSIGN);
		Exp e = exp();
		eat(SEMICOLON);
		return new Assign(i, e);
	}

	ArrayAssign arrayAssign() {
		Identifier i = identifier();
		eat(LBRACKET);
		Exp e1 = exp();
		eat(RBRACKET, EQSIGN);
		Exp e2 = exp();
		eat(SEMICOLON);
		return new ArrayAssign(i, e1, e2);
	}

	// Precedence (.) -> (!) -> (*,/) -> (+,-) -> (<,==) -> (&&) -> (||)

	// CALL -> FACT _CALL
	Exp call() { return _call(factor()); }
	// _CALL -> . (length | identifier(expList)) | empty
	Exp _call(Exp e) { switch(tok.type) {
		eat(TokenType.DOT);
		case LPAREN: eat(LPAREN);
			Identifier i = identifier();
			ExpList el = expList();
			eat(RPAREN); return new Call(e, i, el);
		case LENGTH: eat(LENGTH); return new ArrayLength(e);
		default: return e;
	}}

	// FACT -> TERM _FACT
	Exp factor() { return _factor(term()); }
	// _FACT -> (*|/) FACT | empty
	Exp _factor(Exp e) { switch (tok.type) {
		case TIMES: eat(TokenType.TIMES); return new Times(e, factor());
		case DIV: eat(TokenType.DIV); return new Divide(e, factor());
		default: return e;
	}}

	// TERM -> AND _TERM
	Exp term() { return _term(and()); }
	// _TERM -> (+|-) TERM | empty
	Exp _term(Exp e) { switch(tok.type) {
		case PLUS: eat(TokenType.PLUS); return new Plus(e, term());
		case MINUS: eat(TokenType.MINUS); return new Minus(e, term());
		default: return e;
	}}

	// AND -> OR _AND
	Exp and() { return _and(or()); }
	// _AND -> && AND | empty
	Exp _and(Exp e) { switch (tok.type) {
		case AND: eat(TokenType.AND); return new And(e, and());
		default: return e;
	}}

	// OR -> UNARY _OR
	Exp or() { return _or(unary()); }
	// _OR -> || OR | empty
	Exp _or(Exp e) { switch (tok.type) {
		case OR: eat(TokenType.OR); return new Or(e, or());
		default: return e;
	}}

	Exp unary() { switch (tok.type) {
		case INTLIT:
			int i = ((IntLiteralToken)tok).value;
			eat(INTLIT);return new IntegerLiteral(i);
		case STRINGLIT:
			String s = ((StringLiteralToken)tok).value;
			eat(STRINGLIT); return new IdentifierExp(s);
		case TRUE: eat(TRUE); return new True();
		case FALSE: eat(FALSE); return new False();
		case ID: return new IdentifierExp(identifier().s);
		case THIS: eat(THIS); return new This();
		case NEW: eat(NEW);
			if(tok.type == TokenType.ID) {
				Identifier i = identifier();
				eat(LPAREN, RPAREN);
				return new NewObject(i);
			} else {
				eat(INT, LBRACKET);
				Exp e = exp();
				eat(RBRACKET);
				return new NewArray(e);
			}
		case BANG:
		case LPAREN:
	}}

	Identifier identifier() { switch (tok.type) {
		case ID:
			Identifier i = new Identifier(((IdentifierToken)tok).value);
			eat(ID);
			return i;
		default: error(); return null;
	}}

	ExpList expList() {

	}


}
