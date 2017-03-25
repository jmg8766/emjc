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

class Parser {

	private Lexer input;
	private Token currentToken;
	private java.lang.String outputFile;

	Parser(Lexer lexer) {
		this.input = lexer;
		currentToken = input.next();
		outputFile = lexer.inputFile.substring(0, lexer.inputFile.indexOf('.')) + ".ast";
	}

	void genAstFile() {
		try {
			Program p = parseProgram();
			BufferedWriter out = Files.newBufferedWriter(Paths.get(outputFile));
			out.write(p.accept(new ParseTreePrinter()));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//      catch (SyntaxException e) {
//			System.out.println("\t" + e.getMessage());
//		}
	}

	private void checkType(TokenType t) throws SyntaxException {
		if (!(currentToken.type == t))
			throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected " + t + " instead " +
					"" + "" + "" + "" + "" + "" + "" + "" + "" + "of " + currentToken.type);
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

		ArrayList<Type> types = new ArrayList<>();
		ArrayList<ID> ids = new ArrayList<>();

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
					currentToken = input.next();
					checkType(TokenType.LBRACKET);
					Expression length = parseExpression();
					checkType(TokenType.RBRACKET);
					e = new NewArray(row, col, length);
				} else {
					ID i = parseID();
					checkType(TokenType.LPAREN);
					checkType(TokenType.RPAREN);
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
		return parseOperator(e);
	}

	private Expression parseOperator(Expression lhs) {
		switch (currentToken.type) {
			case AND:
				currentToken = input.next();
				return new And(currentToken.row, currentToken.col, lhs, parseExpression());
			case OR:
				currentToken = input.next();
				return new Or(currentToken.row, currentToken.col, lhs, parseExpression());
			case EQUALS:
				currentToken = input.next();
				return new Equals(currentToken.row, currentToken.col, lhs, parseExpression());
			case LESSTHAN:
				currentToken = input.next();
				return new LessThan(currentToken.row, currentToken.col, lhs, parseExpression());
			case PLUS:
				currentToken = input.next();
				return new Plus(currentToken.row, currentToken.col, lhs, parseExpression());
			case MINUS:
				currentToken = input.next();
				return new Minus(currentToken.row, currentToken.col, lhs, parseExpression());
			case TIMES:
				currentToken = input.next();
				return new Times(currentToken.row, currentToken.col, lhs, parseExpression());
			case DIV:
				currentToken = input.next();
				return new Division(currentToken.row, currentToken.col, lhs, parseExpression());
			case LBRACKET:
				currentToken = input.next();
				Expression indexExpression = parseExpression();
				checkType(TokenType.RBRACKET);
				return parseOperator(new ArrayIndex(currentToken.row, currentToken.col, lhs, indexExpression));
			case DOT:
				currentToken = input.next();
				if (assertType(TokenType.LENGTH)) { //.length
					return parseOperator(new Length(currentToken.row, currentToken.col, lhs));
				}
				else { //.method(arg1, arg2...)
					ID id = parseID();
					checkType(TokenType.LPAREN);
					ArrayList<Expression> params = new ArrayList<>();
					while (currentToken.type != TokenType.RPAREN) {
						params.add(parseExpression());
						assertType(TokenType.COMMA);
					}
					checkType(TokenType.RPAREN);
					lhs = new FunctionCall(lhs, id, params);
				}
				return parseOperator(lhs);
			default:
				return lhs;
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
		checkType(TokenType.BANG);
		return new Not(currentToken.row, currentToken.col, parseExpression());
	}

	private Precedence parsePrecedence() throws SyntaxException {
		checkType(TokenType.LPAREN);
		Precedence i = new Precedence(currentToken.row, currentToken.col, parseExpression());
		checkType(TokenType.RPAREN);
		return i;
	}

	// ================== STATEMENTS ==================================================================================
	private Statement parseStatement(Assignable a) throws SyntaxException {
		int row = currentToken.row;
		int col = currentToken.col;
		switch (currentToken.type) {
			case LBRACE: //BLOCK
				currentToken = input.next();
				ArrayList<Statement> stmts = new ArrayList<>();
				while (!assertType(TokenType.RBRACE)) stmts.add(parseStatement(null));
				return new Block(stmts);
			case IF:
				currentToken = input.next();
				checkType(TokenType.LPAREN);
				Expression expr = parseExpression();
				checkType(TokenType.RPAREN);
				Statement then = parseStatement(null);
				Statement elze = null;
				if (assertType(TokenType.ELSE)) {
					elze = parseStatement(null);
				}
				IfThenElse ifThenElse = new IfThenElse(expr, then, elze);
				ifThenElse.row = row;
				ifThenElse.col = col;
				return ifThenElse;
			case WHILE:
				currentToken = input.next();
				Expression whileExpr = parseExpression();
				Statement whileStmt = parseStatement(null);
				return new While(whileExpr, whileStmt);
			case PRINTLN:
				currentToken = input.next();
				checkType(TokenType.LPAREN);
				Expression printExpression = parseExpression();
				checkType(TokenType.RPAREN);
				checkType(TokenType.SEMICOLON);
				return new Print(printExpression);
			case ID:
				a = parseID();
			case EQSIGN:
			case LBRACKET:
				if (currentToken.type == TokenType.LBRACKET) {
					currentToken = input.next();
					a = new ArrayIndex(currentToken.row, currentToken.col, a, parseExpression());
					checkType(TokenType.RBRACKET);
				}
				checkType(TokenType.EQSIGN);
				Expression idExpr = parseExpression();
				checkType(TokenType.SEMICOLON);
				return new Assign(a, idExpr);
			case SIDEF:
				currentToken = input.next();
				checkType(TokenType.LPAREN);
				Expression sidefExpr = parseExpression();
				checkType(TokenType.RPAREN);
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
		ArrayList<Statement> statements = new ArrayList<>();

		while ((currentToken.type != TokenType.RBRACE && currentToken.type != TokenType.RETURN) && (currentToken.type
				== TokenType.INT || currentToken.type == TokenType.STRING || currentToken.type == TokenType.BOOLEAN ||
				currentToken.type == TokenType.ID)) {

			if (currentToken.type == TokenType.ID) {
				ID i = parseID();
				if (currentToken.type == TokenType.ID) {
					variables.add(new VarDeclaration(i, parseID()));
				} else {
					statements.add(parseStatement(i));
					break;
				}
			} else {
				variables.add(new VarDeclaration(parseType(), parseID()));
			}
			checkType(TokenType.SEMICOLON);
		}

		while (currentToken.type != TokenType.RETURN) {
			statements.add(parseStatement(null));
		}

		checkType(TokenType.RETURN);
		Return returnExpression = new Return(parseExpression());
		checkType(TokenType.SEMICOLON);
		MethodDeclaration method = new MethodDeclaration(type, id, params, variables, statements, returnExpression);
		checkType(TokenType.RBRACE);

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
		checkType(TokenType.SEMICOLON);
		return variable;
	}

	private ClassDeclaration parseClass() throws SyntaxException {
		checkType(TokenType.CLASS);
		int row = currentToken.row;
		int col = currentToken.col;
		ID className = parseID();
		ID parentName = null;
		switch (currentToken.type) {
			case EXTENDS:
				currentToken = input.next();
				parentName = parseID();
				break;
			case LBRACE:
				break;
			default:
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected Block instead " +
						"" + "" + "" + "" + "" + "of " + currentToken.type);
		}
		checkType(TokenType.LBRACE);

		ArrayList<VarDeclaration> variables = new ArrayList<>();
		while (currentToken.type != TokenType.PUBLIC && currentToken.type != TokenType.RBRACE) {
			variables.add(parseVarDeclarations());
		}

		ArrayList<MethodDeclaration> methods = new ArrayList<>();
		while (currentToken.type != TokenType.RBRACE) {
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
		Statement body = parseStatement(null);
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
				throw new SyntaxException(currentToken.row + ":" + currentToken.col + " error: Expected type: Type " +
						"instead " + "of " + currentToken.type);
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
