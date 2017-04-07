import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

public class ASTPrinter implements Visitor<Object> {
	@Override
	public Object visit(Program n) {
		return null;
	}

	@Override
	public Object visit(MainClass n) {
		return null;
	}

	@Override
	public Object visit(ClassDeclSimple n) {
		return null;
	}

	@Override
	public Object visit(ClassDeclExtends n) {
		return null;
	}

	@Override
	public Object visit(VarDecl n) {
		return null;
	}

	@Override
	public Object visit(MethodDecl n) {
		return null;
	}

	@Override
	public Object visit(Formal n) {
		return null;
	}

	@Override
	public Object visit(IntArrayType n) {
		return null;
	}

	@Override
	public Object visit(BooleanType n) {
		return null;
	}

	@Override
	public Object visit(IntegerType n) {
		return null;
	}

	@Override
	public Object visit(StringType n) {
		return null;
	}

	@Override
	public Object visit(IdentifierType n) {
		return null;
	}

	@Override
	public Object visit(Block n) {
		return null;
	}

	@Override
	public Object visit(If n) {
		return null;
	}

	@Override
	public Object visit(While n) {
		return null;
	}

	@Override
	public Object visit(Print n) {
		return null;
	}

	@Override
	public Object visit(Assign n) {
		return null;
	}

	@Override
	public Object visit(ArrayAssign n) {
		return null;
	}

	@Override
	public Object visit(And n) {
		return null;
	}

	@Override
	public Object visit(Or n) {
		return null;
	}

	@Override
	public Object visit(LessThan n) {
		return null;
	}

	@Override
	public Object visit(Equals n) {
		return null;
	}

	@Override
	public Object visit(Plus n) {
		return null;
	}

	@Override
	public Object visit(Minus n) {
		return null;
	}

	@Override
	public Object visit(Times n) {
		return null;
	}

	@Override
	public Object visit(Divide n) {
		return null;
	}

	@Override
	public Object visit(ArrayLookup n) {
		return null;
	}

	@Override
	public Object visit(ArrayLength n) {
		return null;
	}

	@Override
	public Object visit(Call n) {
		return null;
	}

	@Override
	public Object visit(IntegerLiteral n) {
		return null;
	}

	@Override
	public Object visit(StringLiteral n) {
		return null;
	}

	@Override
	public Object visit(True n) {
		return null;
	}

	@Override
	public Object visit(False n) {
		return null;
	}

	@Override
	public Object visit(IdentifierExp n) {
		return null;
	}

	@Override
	public Object visit(This n) {
		return null;
	}

	@Override
	public Object visit(NewArray n) {
		return null;
	}

	@Override
	public Object visit(NewObject n) {
		return null;
	}

	@Override
	public Object visit(Not n) {
		return null;
	}

	@Override
	public Object visit(Identifier n) {
		return null;
	}
}
