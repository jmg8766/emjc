import ast.ID;
import ast.SyntaxException;
import ast.TypeIdList;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import ast.type.Boolean;
import token.*;

import java.util.ArrayList;

public class Parser {

	private Lexer input;
	private Token currentToken;

	Parser(Lexer lexer) throws SyntaxException {
		this.input = lexer;
		currentToken = input.next();
	}

	Program parseProgram() throws SyntaxException {
		MainClassDeclaration main = parseMain();
		ArrayList<ClassDeclaration> classes = new ArrayList<>();
		while (currentToken.type != TokenType.EOF) {
			classes.add(parseClass());
		}
		return new Program(main, classes);
	}

	private TypeIdList parseTypeIdList() throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;

		ArrayList<Type> types = new ArrayList();
		ArrayList<ID> ids = new ArrayList();

		while (currentToken.type != TokenType.RPAREN) {
			types.add(parseType());
			ids.add(parseID());

			if (assertType(TokenType.COMMA)) {    //TODO should we throw error if there is no entry followed by ,
				if (assertType(TokenType.RPAREN)) throw new SyntaxException("");
			}
		}

		TypeIdList param = new TypeIdList(types, ids);
		param.setRow(row);
		param.setCol(col);

		currentToken = input.next();
		return param;
	}




	private void checkType(TokenType type) throws SyntaxException {
		if (!(currentToken.type == type)) {
			throw new SyntaxException("<" + currentToken.row + " >:<" + currentToken.col + " Expected " + type + " " + "instead of " + currentToken.getClass());
		}
		currentToken = input.next();
	}

	private boolean assertType(TokenType type) {
		if (currentToken.type == type) {
			currentToken = input.next();
			return true;
		} else return false;
	}

	/**
	 * Checks that the current token is specific type. Then moves the pointer to the next token.
	 *
	 * @param t : the expected TokenType for the current token
	 * @return : the next token
	 */
	Token parse(TokenType t) {
		if(currentToken.type != t); //error
		Token ret = currentToken;
		currentToken = input.next();
		return ret;
	}

	// ================== EXPRESSIONS =================================================================================
	private Expression parseExpression() throws SyntaxException {
		switch (currentToken.type) {
			case INTLIT:
				return parseIntLiteral();
			case STRINGLIT:
				return new StringLiteral(currentToken.row, currentToken.col, ((StringLiteralToken)currentToken).value);
			case TRUE:
				return new BooleanLiteral(currentToken.row, currentToken.col, true);
			case FALSE:
				return new BooleanLiteral(currentToken.row, currentToken.col, false);
			case ID:
				return parseID();
			case THIS:
				return new This(currentToken.row, currentToken.col);
			case NEW:
				//TODO
				return null;
			case BANG:
				//TODO
				return null;
			case LPAREN:
				//TODO
				return null;
			default:
				Expression expr1 = parseExpression();
				switch (currentToken.type) {
					//TODO
					default:
						return null;
				}


		}
	}

	private ID parseID() {
		ID id = new ID(currentToken.row, currentToken.col, ((IdentifierToken) currentToken).value);
		currentToken = input.next();
		return id;
	}

	private IntLiteral parseIntLiteral() {
		IntLiteral i = new IntLiteral(currentToken.row, currentToken.col, ((IntLiteralToken)currentToken).value);
		currentToken = input.next();
		return i;
	}

	// ================== STATEMENTS ==================================================================================
	private Statement parseStatement() throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;
		switch (currentToken.type) {
			case LBRACKET: //BLOCK
				ArrayList<Statement> stmts = new ArrayList<>();
				while (!assertType(TokenType.RBRACKET)) stmts.add(parseStatement());
				currentToken = input.next();
				return new Block(stmts);
			case IF:
				currentToken = input.next();
				Expression expr = parseExpression();
				checkType(TokenType.LPAREN);
				Statement then = parseStatement();
				Statement elze = null;
				if (assertType(TokenType.ELSE)) {
					elze = parseStatement();
				}
				IfThenElse ifThenElse = new IfThenElse(expr, then, elze);
				ifThenElse.setRow(row);
				ifThenElse.setCol(col);
				return ifThenElse;
			case WHILE:
				currentToken = input.next();
				Expression whileExpr = parseExpression();
				Statement whileStmt = parseStatement();
				return new While(whileExpr, whileStmt);
			case PRINTLN:
				return new Print(parseExpression());
			case ID:
				ID id = parseID();
				if (assertType(TokenType.LBRACE)) //TODO set type
					checkType(TokenType.RBRACE);
				checkType(TokenType.EQSIGN);
				Expression idExpr = parseExpression();
				return new Assign(id, idExpr);
			case SIDEF:
				currentToken = input.next();
				Expression sidefExpr = parseExpression();
				return new Sidef(sidefExpr);
			default:
				//TODO Verify we throw and error
				throw new SyntaxException("");
		}
	}

	private MethodDeclaration parseMethodDeclarations() throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;
		checkType(TokenType.PUBLIC);
		Type type = parseType();
		ID id = parseID();
		checkType(TokenType.LPAREN);


		TypeIdList params = parseTypeIdList();

		checkType(TokenType.RPAREN);
		checkType(TokenType.LBRACE);

		ArrayList<VarDeclaration> variables = new ArrayList<>();
		while (currentToken.type != TokenType.RBRACE) {
			variables.add(parseVarDeclarations());
		}

		ArrayList<Statement> statements = new ArrayList<>();
		while (currentToken.type != TokenType.RETURN) {
			statements.add(parseStatement());
		}

		Return returnExpression = new Return(parseType(), parseExpression());
		MethodDeclaration method = new MethodDeclaration(type, id, params, variables, statements, returnExpression);
		currentToken = input.next();

		method.setRow(row);
		method.setCol(col);
		return method;
	}

	private VarDeclaration parseVarDeclarations() throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;
		VarDeclaration variable = new VarDeclaration(parseType(), parseID());
		variable.setRow(row);
		variable.setCol(col);

		currentToken = input.next();
		return variable;
	}

	private ClassDeclaration parseClass() throws SyntaxException {
		checkType(TokenType.CLASS);
		int row = currentToken.row;
		int col = currentToken.col;

		currentToken = input.next();
		ID className = parseID();
		ID parentName = null;
		switch ((currentToken = input.next()).type) {
			case EXTENDS:
				currentToken = input.next();
				parentName = parseID();
				checkType(TokenType.LBRACE);
				break;
			case LBRACE:
				break;
			default:
				new SyntaxException(""); //TODO Add exception message
		}
		currentToken = input.next();

		ArrayList<VarDeclaration> variables = new ArrayList<>();
		while ((currentToken = input.next()).type != TokenType.PUBLIC && currentToken.type != TokenType.RBRACE) {
			variables.add(parseVarDeclarations());
		}

		ArrayList<MethodDeclaration> methods = new ArrayList<>();
		while ((currentToken = input.next()).type != TokenType.RBRACE) {
			methods.add(parseMethodDeclarations());
		}
		ClassDeclaration claz = new ClassDeclaration(className, parentName, variables, methods);
		claz.setRow(row);
		claz.setCol(col);

		currentToken = input.next();
		return claz;
	}

	private MainClassDeclaration parseMain() throws SyntaxException {
		checkType(TokenType.CLASS);
		int row = currentToken.row;
		int col = currentToken.col;

		checkType(TokenType.ID);
		ID className = parseID();
		checkType(TokenType.LBRACE);
		checkType(TokenType.PUBLIC);
		checkType(TokenType.STATIC);
		checkType(TokenType.VOID);
		checkType(TokenType.MAIN);
		checkType(TokenType.LPAREN);
		checkType(TokenType.STRING);
		checkType(TokenType.LBRACKET);
		checkType(TokenType.RBRACKET);

		currentToken = input.next();
		ID paramName = parseID();
		Statement body = parseStatement();

		checkType(TokenType.RBRACE);

		MainClassDeclaration main = new MainClassDeclaration(className, paramName, body);
		main.setCol(col);
		main.setRow(row);
		currentToken = input.next();

		return main;
	}

// ================== TYPES =======================================================================================
	private Type parseType() throws SyntaxException {
		switch (currentToken.type) {
			case INT:
				if ((currentToken = input.next()).type == TokenType.LBRACKET) {
					currentToken = input.next();
					checkType(TokenType.RBRACKET);
					return new IntArray();
				}
				return new Int();
			case BOOLEAN:
				currentToken = input.next();
				return new Boolean();
			case STRING:
				currentToken = input.next();
				return new ast.type.String();
			case ID:
				currentToken = input.next();
				return parseID();
			default:
				throw new SyntaxException("");
		}
	}
}
