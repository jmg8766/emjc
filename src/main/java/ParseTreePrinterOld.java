import oldast.ID;
import oldast.TypeIdList;
import oldast.Visitor;
import oldast.expression.*;
import oldast.expression.operators.*;
import oldast.statement.*;
import oldast.type.Boolean;
import oldast.type.Int;
import oldast.type.IntArray;
import oldast.type.String;

public class ParseTreePrinterOld implements Visitor<java.lang.String> {

	@Override
	public java.lang.String visit(TypeIdList n) {
		StringBuilder b = new StringBuilder("(TY-ID-LIST ");
		for (int i = 0; i < n.types.size(); i++) {
			b.append("(").append(n.types.get(i).accept(this));
			b.append(" ").append(n.ids.get(i).accept(this)).append(")");
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
		return "(EQSIGN " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Block n) {
		StringBuilder b = new StringBuilder("(BLOCK");
		for (Statement s : n.statements) b.append("\n").append(s.accept(this));
		return b.append(")").toString();
	}

	@Override
	public java.lang.String visit(IfThenElse n) {
		return "(IF " + n.expr.accept(this) + "\n\t" + n.then.accept(this) + "\n\t" + (n.elze == null ? "" : n.elze
				.accept(this)) + "\n)";
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
		return b.append(")").toString();
	}

	@Override
	public java.lang.String visit(VarDeclaration n) {
		return "(VAR-DECL " + n.type.accept(this) + " " + n.id.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(MethodDeclaration n) {
		return "(MTD-DECL " + n.returnType.accept(this) + " " + n.id.accept(this) + " " + n.params.accept(this) + "\n"
				+ n.body.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(MainClassDeclaration n) {
		StringBuilder b = new StringBuilder("(MAIN-CLASS ").append(n.id.accept(this));
		b.append("\n").append("(MAIN-MTD-DECLR ").append(n.args.accept(this));
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
	public java.lang.String visit(IntArray n) {
		return "INT[]";
	}

	@Override
	public java.lang.String visit(NewObject n) {
		return "(NEW-OBJECT " + n.className.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(NewArray n) {
		return "(NEW-INT[]" + n.arrayLength.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(Precedence n) {
		return n.expr.accept(this);
	}

	@Override
	public java.lang.String visit(ArrayIndex n) {
		return "(ARRAY-INDEX " + n.array.accept(this) + " " + n.index.accept(this) + ")";
	}

	@Override
	public java.lang.String visit(This n) {
		return "THIS";
	}

	@Override
	public java.lang.String visit(BooleanLiteral n) {
		return "" + n.value;
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
	public java.lang.String visit(Plus n) {
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
		StringBuilder b = new StringBuilder("(DOT ").append(n.object.accept(this)).append(" (FUN-CALL ").append(n.id
				.accept(this));
		for (Expression e : n.params) b.append(" ").append(e.accept(this));
		return b.append("))").toString();
	}

	@Override
	public java.lang.String visit(Length n) {
		return "(LENGTH)";
	}
}