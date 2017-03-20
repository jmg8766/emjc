import ast.TypeIdList;
import ast.Visitor;
import ast.expression.*;
import ast.statement.*;
import ast.type.Array;
import ast.type.Boolean;
import ast.type.Int;
import ast.type.String;

public class ParseTreePrinter implements Visitor<java.lang.String> {

	@Override
	public java.lang.String visit(TypeIdList n) {
		StringBuilder b = new StringBuilder("(TY-ID-LIST ");
		for (int i = 0; i < n.types.size(); i++) {
			b.append("(").append(n.types.get(i).accept(this));
			b.append(" (").append(n.ids.get(i).accept(this)).append("))");
		}
		return b.append(")").toString();
	}

	@Override
	public java.lang.String visit(Program n) {
		StringBuilder b = new StringBuilder(n.main.accept(this));
		for (ClassDeclaration c : n.classDeclarations) b.append("\n").append(c.accept(this));
		return b.toString();
	}

	@Override
	public java.lang.String visit(Print n) {
		return "(PRINT " + n.msg.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Assign n) {
		return "(EQSIGN " + n.id.accept(this) + " " + n.expr.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Skip n) {
		return "(SKIP)";
	}

	@Override
	public java.lang.String visit(Block n) {
		StringBuilder b = new StringBuilder("(BLOCK\n");
		for (Statement s : n.statements) b.append("\n").append(s.accept(this));
		return b.append(")").toString();
	}

	@Override
	public java.lang.String visit(IfThenElse n) {
		StringBuilder b = new StringBuilder("(IF ").append(n.expr.accept(this));
		b.append("\n\t").append(n.then.accept(this));
		b.append("\n\t").append(n.elze.accept(this));
		return b.append("\n)").toString();
	}

	@Override
	public java.lang.String visit(While n) {
		StringBuilder b = new StringBuilder("(WHILE ").append(n.expr.accept(this));
		b.append("\n\t").append(n.body.accept(this));
		return b.append("\n)").toString();
	}

	@Override
	public java.lang.String visit(Sidef n) {
		return "(SIDEF " + n.expression.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(ClassDeclaration n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.id.accept(this));
		if (n.parent != null) b.append("EXTENDS ").append(n.parent.accept(this));
		for (VarDeclaration v : n.varDeclarations) b.append("\n").append(v.accept(this));
		for (MethodDeclaration m : n.methodDeclarations) b.append("\n").append(m.accept(this));
		return b.append("\n").append(")").toString();
	}

	@Override
	public java.lang.String visit(VarDeclaration n) {
		return "(VAR-DECL " + n.type.accept(this) + " " + n.id.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(MethodDeclaration n) {
		StringBuilder b = new StringBuilder("(MTD-DECL ").append(n.id.accept(this));
		b.append(" ").append(n.params.accept(this)).append("\n");
		b.append(n.body.accept(this)).append("\n");
		return b.append(n.returnExpr.accept(this)).append(")").toString();
	}

	@Override
	public java.lang.String visit(MainClassDeclaration n) {
		StringBuilder b = new StringBuilder("(MAIN-CLASS ").append(n.id.accept(this));
		b.append("\n").append("(MAIN-MTD-DECLR STRING[] ").append(n.args.accept(this));
		b.append("\n").append(n.body.accept(this));
		return b.append("\n").append(")").toString();
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
