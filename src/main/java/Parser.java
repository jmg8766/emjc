import ast.Statement.Block;
import ast.Statement.ClassDeclaration;
import ast.Statement.MainClassDeclaration;
import ast.Statement.Program;
import ast.expression.ID;
import token.IdentifierToken;
import token.Token;
import token.TokenType;

import java.util.ArrayList;

public class Parser {

    private Lexer input;
    private Token token;

    Parser(Lexer lexer) {
        this.input = lexer;
        token = lexer.next();
    }

    Program parseProgram() {
        MainClassDeclaration main = parseMain();
        ArrayList<ClassDeclaration> classes = new ArrayList<>();
        while ((token = input.next()).type != TokenType.EOF) {
            classes.add(parseClass());
        }
        return new Program(main, classes);
    }

    private MainClassDeclaration parseMain() {
        if (token.type != TokenType.CLASS) error();
        token = input.next();
        ID className = parseID();
        if (token.type != TokenType.LBRACE) error();
        if ((token = input.next()).type != TokenType.PUBLIC) error();
        if ((token = input.next()).type != TokenType.STATIC) error();
        if ((token = input.next()).type != TokenType.VOID) error();
        if ((token = input.next()).type != TokenType.MAIN) error();
        if ((token = input.next()).type != TokenType.LPAREN) error();
        if ((token = input.next()).type != TokenType.STRING) error();
        if ((token = input.next()).type != TokenType.LBRACKET) error();
        if ((token = input.next()).type != TokenType.RBRACKET) error();
        token = input.next();
        ID paramName = parseID();
        Block body = parseBlock();
        if (token.type != TokenType.RBRACE) error();
        token = input.next();
        return new MainClassDeclaration(className, paramName, body);
    }

    private ClassDeclaration parseClass() {
        if (token.type != TokenType.CLASS) error();
        token = input.next();
        ID className = parseID();
        ID parentName = null;
        switch ((token = input.next()).type) {
            case EXTENDS:
                token = input.next();
                parentName = parseID();
                if (token.type != TokenType.LBRACE) error();
            case LBRACE:
                break;
            default:
                error();
        }
        token = input.next();
        //TODO


        return null;
    }

    private Block parseBlock() {
        //TODO
        return null;
    }

    private ID parseID() {
        String value = ((IdentifierToken) token).value;
        token = input.next();
        return new ID(value);
    }

    private void error() {
        //TODO
    }
}
