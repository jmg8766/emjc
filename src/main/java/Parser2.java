import ast.statement.*;
import ast.Tree;
import ast.TypeIdList;
import ast.Visitor;
import ast.expression.*;
import ast.type.*;
import ast.type.Boolean;
import ast.type.Class;
import ast.type.String;
import token.Token;

public class Parser2 implements Visitor<Tree> {
	Lexer l;

	public Parser2(Lexer l) {
		this.l = l;
	}

	@Override
	public Tree visit(TypeIdList n) {
		return null;
	}

	@Override
	public Tree visit(Program n) {
		return null;
	}

	@Override
	public Tree visit(Print n) {
		return null;
	}

	@Override
	public Tree visit(Assign n) {
		return null;
	}

	@Override
	public Tree visit(Skip n) {
		return null;
	}

	@Override
	public Tree visit(Block n) {
		return null;
	}

	@Override
	public Tree visit(IfThenElse n) {
		return null;
	}

	@Override
	public Tree visit(While n) {
		return null;
	}

	@Override
	public Tree visit(Sidef n) {
		return null;
	}

	@Override
	public Tree visit(ClassDeclaration n) {
		return null;
	}

	@Override
	public Tree visit(VarDeclaration n) {
		return null;
	}

	@Override
	public Tree visit(MethodDeclaration n) {
		return null;
	}

	@Override
	public Tree visit(MainClassDeclaration n) {
		return null;
	}

	@Override
	public Tree visit(Return n) {
		return null;
	}

	@Override
	public Tree visit(Int n) {
		return null;
	}

	@Override
	public Tree visit(Boolean n) {
		return null;
	}

	@Override
	public Tree visit(String n) {
		return null;
	}

	@Override
	public Tree visit(Array n) {
		return null;
	}

	@Override
	public Tree visit(Class n) {
		return null;
	}

	@Override
	public Tree visit(Method n) {
		return null;
	}

	@Override
	public Tree visit(Var n) {
		return null;
	}

	@Override
	public Tree visit(IntLiteral n) {
		return null;
	}

	@Override
	public Tree visit(StringLiteral n) {
		return null;
	}

	@Override
	public Tree visit(StringPlus n) {
		return null;
	}

	@Override
	public Tree visit(IntPlus n) {
		return null;
	}

	@Override
	public Tree visit(Minus n) {
		return null;
	}

	@Override
	public Tree visit(Times n) {
		return null;
	}

	@Override
	public Tree visit(Division n) {
		return null;
	}

	@Override
	public Tree visit(Modulo n) {
		return null;
	}

	@Override
	public Tree visit(Equals n) {
		return null;
	}

	@Override
	public Tree visit(LessThan n) {
		return null;
	}

	@Override
	public Tree visit(And n) {
		return null;
	}

	@Override
	public Tree visit(Or n) {
		return null;
	}

	@Override
	public Tree visit(Neg n) {
		return null;
	}

	@Override
	public Tree visit(Not n) {
		return null;
	}

	@Override
	public Tree visit(ID n) {
		return null;
	}

	@Override
	public Tree visit(FunctionCall n) {
		return null;
	}
}
