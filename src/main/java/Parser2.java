import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.list.*;
import ast.statement.*;
import ast.type.*;
import token.*;

import static token.TokenType.*;

public class Parser2 {

	private final Lexer l;
	private Token tok;

	public Parser2(Lexer l) { this.l = l; tok = l.next(); }
	private void eat(TokenType... t) { for (TokenType aT : t) if (tok.type == aT) tok = l.next(); else error(); }
	private void error() { System.out.println(tok.col + ":" + tok.row + " error: ...");}

	Program program() { return new Program(main(), classDeclList()); }

	private MainClass main() {
		eat(CLASS);
		Identifier i1 = identifier();
		eat(LBRACE, PUBLIC, STATIC, VOID, MAIN, LPAREN, STRING, LBRACKET, RBRACKET);
		Identifier i2 = identifier();
		eat(RPAREN, LBRACE);
		Statement s = statement();
		eat(RBRACE, RBRACE);
		return new MainClass(i1, i2, s);
	}

	private ClassDecl classDecl() {
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

	private VarDecl varDecl() { return _varDecl(type()); }
	private VarDecl _varDecl(Type t) {
		VarDecl v = new VarDecl(t, identifier());
		eat(SEMICOLON); return v;
	}

	private MethodDecl methodDecl() {
		eat(PUBLIC); Type t = type(); Identifier i = identifier();
		eat(LPAREN); FormalList fl = formalList(); eat(RPAREN, LBRACE);
			VarDeclList vl = new VarDeclList();
			StatementList sl = new StatementList();
			while(true) { switch(tok.type) {
				case INT: case BOOLEAN: case STRING:
					vl.list.add(varDecl());
					continue;
				case ID:
					Identifier id = identifier();
					if(tok.type != ID) {
						sl.list.add(_assign(id)); break;
					}
					vl.list.add(_varDecl(new IdentifierType(id)));
					continue;
				default: break;
			} break; }
			sl.list.addAll(statementList().list);
			eat(RETURN); Exp e = exp();
		eat(SEMICOLON, RBRACE);
		return new MethodDecl(t, i, fl, vl, sl, e);
	}

	Formal formal() { return new Formal(type(), identifier()); }

	Type type() { switch (tok.type) {
			case BOOLEAN: eat(BOOLEAN); return new BooleanType();
			case STRING: eat(STRING); return new StringType();
			case INT: eat(TokenType.INT);
				switch (tok.type) {
					case LBRACKET: eat(RBRACKET); return new IntArrayType();
					default: return new IntegerType();
				}
			case ID: return new IdentifierType(identifier());
			default: error(); return null;
	}}

	Identifier identifier() { switch (tok.type) {
		case ID:
			Identifier i = new Identifier(((IdentifierToken)tok).value);
			eat(ID); return i;
		default: error(); return null;
	}}

	// ===== STATEMENTS =========

	Statement statement() { switch(tok.type) {
		case LBRACE: return block();
		case IF: return ifStmnt();
		case WHILE: return whileStmnt();
		case PRINTLN: return print();
		case ID: return assign();
		case SIDEF:
			eat(SIDEF, LPAREN); Exp e = exp();
			eat(RPAREN, SEMICOLON); return e;
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

	Statement assign() { return _assign(identifier()); }
	Statement _assign(Identifier i) { switch (tok.type) {
		case EQSIGN:
			eat(EQSIGN);
			Exp e = exp();
			eat(SEMICOLON);
			return new Assign(i, e);
		case LBRACKET:
			eat(LBRACKET); Exp e1 = exp();
			eat(RBRACKET, EQSIGN); Exp e2 = exp();
			eat(SEMICOLON); return new ArrayAssign(i, e1, e2);
		default: error(); return null;
	}}

	// ===== EXPRESSIONS =========
	// Precedence (.) -> (!) -> (*,/) -> (+,-) -> (<,==) -> (&&) -> (||)

	// EXP -> OR _OR
	Exp exp() { return _or(or());}

	// OR -> AND _OR
	Exp or() { return _or(and()); }
	// _OR -> || OR | empty
	Exp _or(Exp e) { switch (tok.type) {
		case OR: eat(TokenType.OR); return new Or(e, or());
		default: return e;
	}}

	// AND -> LTEQ _AND
	Exp and() { return _and(ltEq()); }
	// _AND -> && AND | empty
	Exp _and(Exp e) { switch (tok.type) {
		case AND: eat(TokenType.AND); return new And(e, and());
		default: return e;
	}}

	// LTEQ -> TERM _LTEQ
	Exp ltEq() { return _ltEq(term()); }
	// _LTEQ -> (< LTEQ) | (== LTEQ) | empty
	Exp _ltEq(Exp e) { switch(tok.type) { //TODO
		case LESSTHAN:
		case EQUALS:
		default: error(); return null;
	}}

	// TERM -> FACT _TERM
	Exp term() { return _term(factor()); }
	// _TERM -> (+|-) TERM | empty
	Exp _term(Exp e) { switch(tok.type) {
		case PLUS: eat(TokenType.PLUS); return new Plus(e, term());
		case MINUS: eat(TokenType.MINUS); return new Minus(e, term());
		default: return e;
	}}

	// FACT -> CALL _FACT
	Exp factor() { return _factor(call()); }
	// _FACT -> (* | /) FACT | empty
	Exp _factor(Exp e) { switch (tok.type) {
		case TIMES: eat(TokenType.TIMES); return new Times(e, factor());
		case DIV: eat(TokenType.DIV); return new Divide(e, factor());
		default: return e;
	}}

	// NOT -> (! EXP) | EXP
	Exp not() { switch (tok.type) {
		case BANG: eat(BANG); return new Not(exp());
		default: return call();
	}}

	// CALL -> FACTOR _EXP
	Exp call() { return _call(factor()); }
	// _CALL -> . (length | identifier(expList)) | empty
	Exp _call(Exp e) { switch(tok.type) {
		case DOT: eat(DOT);
			if(tok.type == LPAREN) {
				eat(LPAREN); Identifier i = identifier();
				ExpList el = expList(); eat(RPAREN);
				return new Call(e, i, el);
			} else {
				eat(LENGTH); return new ArrayLength(e);
			}
		default: return e;
	}}

	// UNARY -> (! EXP) | (new identifier()) | (new int[EXP]) | (this) | identifier | false | true | StringLit | IntLit
	Exp unary() { switch (tok.type) {
		case INTLIT:
			int val = ((IntLiteralToken)tok).value;
			eat(INTLIT);return new IntegerLiteral(val);
		case STRINGLIT:
			String s = ((StringLiteralToken)tok).value;
			eat(STRINGLIT); return new StringLiteral(s);
		case TRUE:
			eat(TRUE); return new True();
		case FALSE:
			eat(FALSE); return new False();
		case ID:
			return new IdentifierExp(identifier());
		case THIS:
			eat(THIS); return new This();
		case NEW:
			eat(NEW);
			if(tok.type == TokenType.ID) {
				Identifier i = identifier();
				eat(LPAREN, RPAREN); return new NewObject(i);
			} else {
				eat(INT, LBRACKET); Exp e = exp();
				eat(RBRACKET); return new NewArray(e);
			}
		case LPAREN:
			eat(LPAREN); Exp e = exp();
			eat(RPAREN); return e;
		default:
			error(); return null;
	}}

	// ===== LISTS =========

	ExpList expList() {
		ExpList e = new ExpList();
		while(tok.type != RPAREN) e.list.add(exp());
		return e;
	}

	StatementList statementList() {
		StatementList s = new StatementList();
		while(tok.type != RBRACE) s.list.add(statement());
		return s;
	}

	ClassDeclList classDeclList() {
		ClassDeclList cl = new ClassDeclList();
		while(tok.type != EOF) cl.list.add(classDecl());
		return cl;
	}

	FormalList formalList() {
		FormalList fl = new FormalList();
		while(tok.type != RPAREN) fl.list.add(formal());
		return fl;
	}

	// only works when called from ClassDecl
	VarDeclList varDeclList() {
		VarDeclList vl = new VarDeclList();
		while(tok.type != PUBLIC) vl.list.add(varDecl());
		return vl;
	}

	MethodDeclList methodDeclList() {
		MethodDeclList ml = new MethodDeclList();
		while(tok.type != RBRACE) ml.list.add(methodDecl());
		return ml;
	}

}
