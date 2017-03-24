import ast.ID;
import ast.SyntaxException;
import ast.TypeIdList;
import ast.expression.*;
import ast.expression.operators.*;
import ast.statement.*;
import ast.type.Boolean;
import ast.type.*;
import ast.type.String;
import token.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {

	private Lexer input;
	private Token currentToken;
	private java.lang.String outputFile;

	Parser(Lexer lexer) {
		this.input = lexer;
		currentToken = input.next();
		outputFile = lexer.inputFile.substring(0, lexer.inputFile.indexOf('.')) + ".ast";
	}

	void genAstFile() {
		try (BufferedWriter out = Files.newBufferedWriter(Paths.get(outputFile))) {
			out.write(parseProgram().accept(new ParseTreePrinter()));
		} catch (IOException e) {
			System.out.println("An IO error has occurred");
		}
	}

	/**
	 * Checks that the current token has type t, then move the pointer forward
	 *
	 * @param t
	 * @throws SyntaxException
	 */
	private void checkType(TokenType t) throws SyntaxException {
		if (!(currentToken.type == t))
			throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected " + t + " instead " +
					"" + "" + "" + "of " + currentToken.type);
		currentToken = input.next();
	}

	private boolean assertType(TokenType type) {
		if (currentToken.type == type) {
			currentToken = input.next();
			return true;
		} else return false;
	}

	private Program parseProgram() throws SyntaxException {
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
			if (assertType(TokenType.COMMA)) {
				if (assertType(TokenType.RPAREN))
					throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected " +
							"Parameters" + " after , found none");
			}
		}

		TypeIdList param = new TypeIdList(types, ids);
		param.row = row;
		param.col = col;

		currentToken = input.next();
		return param;
	}

	// ================== EXPRESSIONS =================================================================================
	private Expression parseExpression() throws SyntaxException {
		Expression e;
		switch (currentToken.type) {
			case INTLIT:
				e = parseIntLiteral();
				break;
			case STRINGLIT:
				e = parseStringLiteral();
				break;
			case TRUE:
				e = parseTrue();
				break;
			case FALSE:
				e = parseFalse();
				break;
			case ID:
				e = parseID();
				break;
			case THIS:
				e = parseThis();
				break;
			case NEW:
				int row = currentToken.row;
				int col = currentToken.col;
				currentToken = input.next();
				if (currentToken.type == TokenType.INT) {
					assertType(TokenType.LBRACKET);
					Expression length = parseExpression();
					assertType(TokenType.RBRACKET);
					e = new NewArray(row, col, length);
				} else {
					ID i = parseID();
					assertType(TokenType.LPAREN);
					assertType(TokenType.RPAREN);
					e = new NewObject(row, col, i);
				}
				break;
			case BANG:
				e = parseNot();
				break;
			case LPAREN:
				e = parsePrecedence();
				break;
			default:
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected an " +
						"Expression instead of " + currentToken.type);
		}
		switch (currentToken.type) {
			case AND:
				currentToken = input.next();
				return new And(currentToken.row, currentToken.col, e, parseExpression());
			case OR:
				currentToken = input.next();
				return new Or(currentToken.row, currentToken.col, e, parseExpression());
			case EQUALS:
				currentToken = input.next();
				return new Equals(currentToken.row, currentToken.col, e, parseExpression());
			case LESSTHAN:
				currentToken = input.next();
				return new LessThan(currentToken.row, currentToken.col, e, parseExpression());
			case PLUS:
				currentToken = input.next();
				return new Plus(currentToken.row, currentToken.col, e, parseExpression());
			case MINUS:
				currentToken = input.next();
				return new Minus(currentToken.row, currentToken.col, e, parseExpression());
			case TIMES:
				currentToken = input.next();
				return new Times(currentToken.row, currentToken.col, e, parseExpression());
			case DIV:
				currentToken = input.next();
				return new Division(currentToken.row, currentToken.col, e, parseExpression());
			case LBRACKET:
				currentToken = input.next();
				Expression expr2 = parseExpression();
				checkType(TokenType.RBRACKET);
				return new ArrayIndex(currentToken.row, currentToken.col, e, expr2);
			case DOT:
				currentToken = input.next();
				if (assertType(TokenType.LENGTH)) return new Length(currentToken.row, currentToken.col, e);
				else { //Function call
					ID id = parseID();
					checkType(TokenType.LPAREN);
					ArrayList<Expression> params = new ArrayList<>();
					while ((currentToken = input.next()).type != TokenType.RPAREN) {
						params.add(parseExpression());
						assertType(TokenType.COMMA);
					}
					return new FunctionCall(e, id, params);
				}
			default:
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected an " +
						"Expression instead of " + currentToken.type);
		}
	}

	private ID parseID() throws SyntaxException {
		if (currentToken.type != TokenType.ID)
			throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: expected an ID intead of "
					+ currentToken.type);
		ID id = new ID(currentToken.row, currentToken.col, ((IdentifierToken) currentToken).value);
		currentToken = input.next();
		return id;
	}

	private IntLiteral parseIntLiteral() {
		IntLiteral i = new IntLiteral(currentToken.row, currentToken.col, ((IntLiteralToken) currentToken).value);
		currentToken = input.next();
		return i;
	}

	private StringLiteral parseStringLiteral() {
		StringLiteral i = new StringLiteral(currentToken.row, currentToken.col, ((StringLiteralToken) currentToken)
				.value);
		currentToken = input.next();
		return i;
	}

	private BooleanLiteral parseTrue() {
		BooleanLiteral i = new BooleanLiteral(currentToken.row, currentToken.col, true);
		currentToken = input.next();
		return i;
	}

	private BooleanLiteral parseFalse() {
		BooleanLiteral i = new BooleanLiteral(currentToken.row, currentToken.col, false);
		currentToken = input.next();
		return i;
	}

	private This parseThis() {
		This i = new This(currentToken.row, currentToken.col);
		currentToken = input.next();
		return i;
	}

	private Not parseNot() throws SyntaxException {
		return new Not(currentToken.row, currentToken.col, parseExpression());
	}

	private Precedence parsePrecedence() throws SyntaxException {
		Precedence i = new Precedence(currentToken.row, currentToken.col, parseExpression());
		checkType(TokenType.RPAREN);
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
				ifThenElse.row = row;
				ifThenElse.col = col;
				return ifThenElse;
			case WHILE:
				currentToken = input.next();
				Expression whileExpr = parseExpression();
				Statement whileStmt = parseStatement();
				return new While(whileExpr, whileStmt);
			case PRINTLN:
				currentToken = input.next();
				checkType(TokenType.LPAREN);
				Expression printExpression = parseExpression();
				checkType(TokenType.RPAREN);
				checkType(TokenType.SEMICOLON);
				return new Print(printExpression);
			case ID:
				Assignable a = parseID();
				if (currentToken.type == TokenType.LBRACE) {
					currentToken = input.next();
					a = new ArrayIndex(currentToken.row, currentToken.col, a, parseExpression());
					checkType(TokenType.RBRACE);
				}
				checkType(TokenType.EQSIGN);
				Expression idExpr = parseExpression();
				checkType(TokenType.SEMICOLON);
				return new Assign(a, idExpr);
			case SIDEF:
				currentToken = input.next();
				Expression sidefExpr = parseExpression();
				checkType(TokenType.SEMICOLON);
				return new Sidef(sidefExpr);
			default:
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected Statement " +
						"instead of " + currentToken.type);
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

		method.row = row;
		method.col = col;
		return method;
	}

	private VarDeclaration parseVarDeclarations() throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;
		VarDeclaration variable = new VarDeclaration(parseType(), parseID());
		variable.row = row;
		variable.col = col;
		currentToken = input.next();
		return variable;
	}

	private ClassDeclaration parseClass() throws SyntaxException {
		checkType(TokenType.CLASS);
		int row = currentToken.row;
		int col = currentToken.col;
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
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected Block instead " +
						"" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "of " + currentToken.type);
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
		claz.row = row;
		claz.col = col;

		currentToken = input.next();
		return claz;
	}

	private MainClassDeclaration parseMain() throws SyntaxException {
		checkType(TokenType.CLASS);
		int row = currentToken.row;
		int col = currentToken.col;

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

		ID paramName = parseID();
		checkType(TokenType.RPAREN);
		checkType(TokenType.LBRACE);
		Statement body = parseStatement();
		checkType(TokenType.RBRACE);
		checkType(TokenType.RBRACE);

		MainClassDeclaration main = new MainClassDeclaration(className, paramName, body);
		main.col = col;
		main.row = row;
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
				return parseBoolean();
			case STRING:
				return parseString();
			case ID:
				return parseID();
			default:
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected Type instead "
						+ "of " + currentToken.type);
		}
	}

	private Boolean parseBoolean() {
		Boolean i = new Boolean(currentToken.row, currentToken.col);
		currentToken = input.next();
		return i;
	}

	private ast.type.String parseString() {
		String i = new String(currentToken.row, currentToken.col);
		currentToken = input.next();
		return i;
	}
}
