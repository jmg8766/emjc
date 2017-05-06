import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.list.*;
import ast.statement.*;
import ast.type.*;
import org.testng.Assert;
import token.*;

import static token.TokenType.*;

public class Parser {

    private final Lexer l;
    private Token tok;

    public Parser(Lexer l) {
        this.l = l;
        tok = l.next();
    }

    private void eat(TokenType... t) {
        for (TokenType aT : t) {
            if (tok.type == aT) tok = l.next();
            else error("Unexpected Token: [" + tok.type + "] Expecting: [" + aT + "]");
        }
    }

    private void error(String s) {
        System.out.println(tok.row + ":" + tok.col + " error: " + s);
        Assert.fail();
        System.exit(0);
    }

    Program program() {
        return new Program(main(), classDeclList());
    }

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
        ClassDecl c = null;
        switch (tok.type) {
            case EXTENDS:
                eat(EXTENDS);
                Identifier i2 = identifier();
                eat(LBRACE);
                c = new ClassDeclExtends(tok.row + ":" + tok.col, i1, i2, varDeclList(), methodDeclList());
                break;
            case LBRACE:
                eat(LBRACE);
                c = new ClassDeclSimple(tok.row + ":" + tok.col, i1, varDeclList(), methodDeclList());
                break;
            default:
                error("Unexpected token: [" + tok + "] Expected: [" + EXTENDS + "] or [" + LBRACE + "]");
                return null;
        }
        eat(RBRACE);
        return c;
    }

    private VarDecl varDecl() {
        return _varDecl(type());
    }

    private VarDecl _varDecl(Type t) {
        VarDecl v = new VarDecl(t, identifier());
        eat(SEMICOLON);
        return v;
    }

    private MethodDecl methodDecl() {
        eat(PUBLIC);
        Type t = type();
        Identifier i = identifier();
        eat(LPAREN);
        FormalList fl = formalList();
        eat(RPAREN, LBRACE);
        VarDeclList vl = new VarDeclList();
        StatementList sl = new StatementList();
        while (true) {
            switch (tok.type) {
                case INT:
                case BOOLEAN:
                case STRING:
                    vl.list.add(varDecl());
                    continue;
                case ID:
                    Identifier id = identifier();
                    if (tok.type != ID) {
                        sl.list.add(_assign(id));
                        break;
                    }
                    vl.list.add(_varDecl(IdentifierType.getInstance(id)));
                    continue;
                default:
                    break;
            }
            break;
        }
        sl.list.addAll(statementList().list);
        eat(RETURN);
        Exp e = exp();
        eat(SEMICOLON, RBRACE);
        return new MethodDecl(tok.row + ":" + tok.col, t, i, fl, vl, sl, e);
    }

    Formal formal() {
        return new Formal(tok.row + ":" + tok.col, type(), identifier());
    }

    Type type() {
        switch (tok.type) {
            case BOOLEAN:
                eat(BOOLEAN);
                return BooleanType.getInstance();
            case STRING:
                eat(STRING);
                return StringType.getInstance();
            case INT:
                eat(TokenType.INT);
                switch (tok.type) {
                    case LBRACKET:
                        eat(LBRACKET, RBRACKET);
                        return IntArrayType.getInstance();
                    default:
                        return IntegerType.getInstance();
                }
            case ID:
                return IdentifierType.getInstance(identifier());
            default:
                error("Unexpected token: [" + tok.type + "] Expected: [" + BOOLEAN + "] or [" + STRING + "] or ["
                        + INT + "] or [" + ID + "]");
                return null;
        }
    }

    Identifier identifier() {
        switch (tok.type) {
            case ID:
                Identifier i = new Identifier(tok.row + ":" + tok.col, ((IdentifierToken) tok).value);
                eat(ID);
                return i;
            default:
                error("Unexpected token: [" + tok.type + " Expected: [" + ID + "]");
                return null;
        }
    }

    // ===== STATEMENTS =========

    Statement statement() {
        switch (tok.type) {
            case LBRACE:
                return block();
            case IF:
                return ifStmnt();
            case WHILE:
                return whileStmnt();
            case PRINTLN:
                return print();
            case ID:
                return assign();
            case SIDEF:
                eat(SIDEF, LPAREN);
                Exp e = new Sidef(tok.row + ":" + tok.col, exp());
                eat(RPAREN, SEMICOLON);
                return e;
            default:
                error("Unexpected token: [" + tok.type + "] Expected a Statement");
                return null;
        }
    }

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
        if (tok.type != ELSE) return new If(tok.row + ":" + tok.col, e, s1, null);
        eat(ELSE);
        return new If(tok.row + ":" + tok.col, e, s1, statement());
    }

    While whileStmnt() {
        eat(WHILE, LPAREN);
        Exp e = exp();
        eat(RPAREN);
        return new While(tok.row + ":" + tok.col, e, statement());
    }

    Print print() {
        eat(PRINTLN, LPAREN);
        Exp e = exp();
        eat(RPAREN, SEMICOLON);
        return new Print(tok.row + ":" + tok.col, e);
    }

    Statement assign() {
        return _assign(identifier());
    }

    Statement _assign(Identifier i) {
        switch (tok.type) {
            case EQSIGN:
                String pos = tok.row + ":" + tok.col;
                eat(EQSIGN);
                Exp e = exp();
                eat(SEMICOLON);
                return new Assign(pos, i, e);
            case LBRACKET:
                eat(LBRACKET);
                Exp e1 = exp();
                eat(RBRACKET, EQSIGN);
                Exp e2 = exp();
                eat(SEMICOLON);
                return new ArrayAssign(i, e1, e2);
            default:
                error("Unexpected Token: [" + tok.type + "] Expecting: [" + EQSIGN + "] or [" + LBRACKET + "]");
                return null;
        }
    }

    // ===== EXPRESSIONS =========
    // Precedence (unary) -> (. []) -> (!) -> (*,/) -> (+,-) -> (<,==) -> (&&) -> (||)

    // EXP -> OR _OR
    Exp exp() { return _or(or()); }

    // OR -> AND _OR
    Exp or() {
        return _or(and());
    }
    // _OR -> || OR | empty
    Exp _or(Exp e) {
        switch (tok.type) {
            case OR:
                eat(TokenType.OR);
                return new Or(tok.row + ":" + tok.col, e, or());
            default:
                return e;
        }
    }

    // AND -> LTEQ _AND
    Exp and() {
        return _and(ltEq());
    }
    // _AND -> && AND | empty
    Exp _and(Exp e) {
        switch (tok.type) {
            case AND:
                eat(TokenType.AND);
                return new And(tok.row + ":" + tok.col, e, and());
            default:
                return e;
        }
    }

    // LTEQ -> TERM _LTEQ
    Exp ltEq() {
        return _ltEq(term());
    }
    // _LTEQ -> (< LTEQ) | (== LTEQ) | empty
    Exp _ltEq(Exp e) {
        switch (tok.type) {
            case LESSTHAN:
                eat(LESSTHAN);
                return new LessThan(tok.row + ":" + tok.col, e, ltEq());
            case EQUALS:
                eat(EQUALS);
                return new Equals(tok.row + ":" + tok.col, e, ltEq());
            default:
                return e;
        }
    }

    // TERM -> FACT _TERM
    Exp term() {
        return _term(factor());
    }
    // _TERM -> (+|-) TERM | empty
    Exp _term(Exp e) {
        switch (tok.type) {
            case PLUS:
                eat(TokenType.PLUS);
                return new Plus(tok.row + ":" + tok.col, e, term());
            case MINUS:
                eat(TokenType.MINUS);
                return new Minus(tok.row + ":" + tok.col, e, term());
            default:
                return e;
        }
    }

    // FACT -> NOT _FACT
    Exp factor() {
        return _factor(not());
    }
    // _FACT -> (* | /) FACT | empty
    Exp _factor(Exp e) {
        switch (tok.type) {
            case TIMES:
                eat(TokenType.TIMES);
                return new Times(tok.row + ":" + tok.col, e, factor());
            case DIV:
                eat(TokenType.DIV);
                return new Divide(tok.row + ":" + tok.col, e, factor());
            default:
                return e;
        }
    }

    // NOT -> (! EXP) | EXP
    Exp not() {
        switch (tok.type) {
            case BANG:
                eat(BANG);
                return new Not(tok.row + ":" + tok.col, not());
            default:
                return call();
        }
    }

    // CALL -> UNARY _EXP
    Exp call() {
        return _call(unary());
    }

    // _CALL -> . (length | identifier(expList)) | empty
    Exp _call(Exp e) {
        switch (tok.type) {
            case DOT:
                eat(DOT);
                if (tok.type == ID) {
                    Identifier i = identifier();
                    eat(LPAREN);
                    ExpList el = expList();
                    eat(RPAREN);
                    return _call(new Call(tok.row + ":" + tok.col, e, i, el));
                } else {
                    eat(LENGTH);
                    return _call(new ArrayLength(tok.row + ":" + tok.col, e));
                }
            case LBRACKET:
                eat(LBRACKET);
                Exp e2 = exp();
                eat(RBRACKET);
                return new ArrayLookup(tok.row + ":" + tok.col, e, e2);
            default:
                return e;
        }
    }

    // UNARY -> (! EXP) | (new identifier()) | (new int[EXP]) | (this) | identifier | false | true | StringLit | IntLit
    Exp unary() {
        switch (tok.type) {
            case INTLIT:
                int val = ((IntLiteralToken) tok).value;
                eat(INTLIT);
                return new IntegerLiteral(tok.row + ":" + tok.col, val);
            case STRINGLIT:
                String s = ((StringLiteralToken) tok).value;
                eat(STRINGLIT);
                return new StringLiteral(tok.row + ":" + tok.col, s);
            case TRUE:
                eat(TRUE);
                return new True(tok.row + ":" + tok.col);
            case FALSE:
                eat(FALSE);
                return new False(tok.row + ":" + tok.col);
            case ID:
                return new IdentifierExp(tok.row + ":" + tok.col, identifier());
            case THIS:
                eat(THIS);
                return new This(tok.row + ":" + tok.col);
            case NEW:
                eat(NEW);
                if (tok.type == TokenType.ID) {
                    Identifier i = identifier();
                    eat(LPAREN, RPAREN);
                    return new NewObject(tok.row + ":" + tok.col, i);
                } else {
                    eat(INT, LBRACKET);
                    Exp e = exp();
                    eat(RBRACKET);
                    return new NewArray(tok.row + ":" + tok.col, e);
                }
            case LPAREN:
                eat(LPAREN);
                Exp e = exp();
                eat(RPAREN);
                return e;
            default:
                error("Unexpected Token: [" + tok.type + "] Expected an expression");
                return null;
        }
    }

    // ===== LISTS =========

    ExpList expList() {
        ExpList e = new ExpList();
        while (tok.type != RPAREN) {
            e.list.add(exp());
            if (tok.type == COMMA) eat(COMMA);
        }
        return e;
    }

    StatementList statementList() {
        StatementList s = new StatementList();
        while (tok.type != RETURN && tok.type != RBRACE) s.list.add(statement());
        return s;
    }

    ClassDeclList classDeclList() {
        ClassDeclList cl = new ClassDeclList();
        while (tok.type != EOF) cl.list.add(classDecl());
        return cl;
    }

    FormalList formalList() {
        FormalList fl = new FormalList();
        while (tok.type != RPAREN) {
            fl.list.add(formal());
            if (tok.type == COMMA) eat(COMMA);
        }
        return fl;
    }

    // only works when called from ClassDecl
    VarDeclList varDeclList() {
        VarDeclList vl = new VarDeclList();
        while (tok.type != PUBLIC && tok.type != RBRACE) vl.list.add(varDecl());
        return vl;
    }

    MethodDeclList methodDeclList() {
        MethodDeclList ml = new MethodDeclList();
        while (tok.type != RBRACE) ml.list.add(methodDecl());
        return ml;
    }

}
