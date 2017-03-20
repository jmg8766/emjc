import ast.TypeIdList;
import ast.Visitor;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import ast.type.Boolean;
import ast.type.String;

public class ParseTreePrinter implements Visitor<java.lang.String> {

	@Override
	public java.lang.String visit(TypeIdList n) {
		return null;
	}

	@Override
	public java.lang.String visit(Program n) {
		return null;
	}

	@Override
	public java.lang.String visit(Print n) {
		return null;
	}

	@Override
	public java.lang.String visit(Assign n) {
		return null;
	}

	@Override
	public java.lang.String visit(Skip n) {
		return null;
	}

	@Override
	public java.lang.String visit(Block n) {
		return null;
	}

	@Override
	public java.lang.String visit(IfThenElse n) {
		return null;
	}

	@Override
	public java.lang.String visit(While n) {
		return null;
	}

	@Override
	public java.lang.String visit(Sidef n) {
		return null;
	}

	@Override
	public java.lang.String visit(ClassDeclaration n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.id.accept(this));
		if(n.parent != null) b.append("EXTENDS ").append(n.parent.accept(this));
		for (VarDeclaration v : n.varDeclarations) b.append("\n").append(v.accept(this));
		for (MethodDeclaration m : n.methodDeclarations) b.append("\n").append(m.accept(this));
		return b.append("\n").append(")").toString();
	}

	@Override
	public java.lang.String visit(VarDeclaration n) {
		return null;
	}

	@Override
	public java.lang.String visit(MethodDeclaration n) {
		return null;
	}

	@Override
	public java.lang.String visit(MainClassDeclaration n) {
		return null;
	}

	@Override
	public java.lang.String visit(Return n) {
		return "(RETURN " + n.returnValue.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Int n) {
		return "INT";
	}

	@Override
	public java.lang.String visit(Boolean n) {
		return "BOOLEAN";
	}

	@Override
	public java.lang.String visit(String n) {
		return "STRING";
	}

	@Override
	public java.lang.String visit(Array n) {
		return null; //TODO
	}

	@Override
	public java.lang.String visit(IntLiteral n) {
		return "(INTLIT " + n.value + ")";
	}

	@Override
	public java.lang.String visit(StringLiteral n) {
		return "(STRINGLIT " + n.val + ")";
	}

	@Override
	public java.lang.String visit(StringPlus n) {
		return "(+ " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(IntPlus n) {
		return "(+ " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Minus n) {
		return "(- " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Times n) {
		return "(* " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Division n) {
		return "(/ " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Modulo n) {
		return "(% " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Equals n) {
		return "(== " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(LessThan n) {
		return "(< " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(And n) {
		return "(&& " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Or n) {
		return "(|| " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Not n) {
		return "(NOT " + n.expr.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(ID n) {
		return "(ID " + n.id + ")";
	}

	@Override
	public java.lang.String visit(FunctionCall n) {
		StringBuilder b = new StringBuilder("(FUN-CALL ").append(n.id.accept(this));
		for (Expression e : n.params) b.append(" ").append(e.accept(this));
		return b.append(")").toString();
	}
}
