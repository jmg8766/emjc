import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

public class ASTPrinter implements Visitor<String> {

	private int tabs = 0;

	private String tabs() {
		StringBuilder b = new StringBuilder();
		for (int i = tabs; i > 0; --i) b.append("\t");
		return b.toString();
	}

	public String visit(Program n) {
		StringBuilder b = new StringBuilder(n.m.accept(this));
		n.cl.list.forEach(c -> b.append("\n\n").append(c.accept(this)));
		return b.toString();
	}

	public String visit(MainClass n) {
		StringBuilder b = new StringBuilder("(MAIN-CLASS ").append(n.i.accept(this)).append("\n(MAIN-MTD-DECLR ")
				.append(n.i2.accept(this)).append(" (BLOCK\n");
		++tabs;
		b.append(n.s.accept(this)).append("\n))");
		--tabs;
		return b.toString();
	}

	public String visit(ClassDeclSimple n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.i.accept(this));
		n.vl.list.forEach(v -> b.append("\n").append(v.accept(this)));
		n.ml.list.forEach(m -> b.append("\n").append(m.accept(this)));
		return b.append(")").toString();
	}

	public String visit(ClassDeclExtends n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.i.accept(this)).append("EXTENDS ").append(n.parent
				.accept(this));
		n.vl.list.forEach(v -> b.append("\n").append(v.accept(this)));
		n.ml.list.forEach(m -> b.append("\n").append(m.accept(this)));
		return b.append(")").toString();
	}

	public String visit(VarDecl n) {
		return tabs() + "(VAR-DECL " + n.t.accept(this) + " " + n.i.accept(this) + ")";
	}

	public String visit(MethodDecl n) {
		StringBuilder b = new StringBuilder("(MTD-DECL ").append(n.t.accept(this)).append(" ").append(n.i.accept(this)
		).append(" ").append("(TY-ID-LIST ");
		n.fl.list.forEach(f -> b.append(f.accept(this)));
		b.append(")(BLOCK\n");
		++tabs;
		n.vl.list.forEach(v -> b.append(v.accept(this)));
		b.append("\n");
		n.sl.list.forEach(s -> b.append(s.accept(this)));
		b.append(tabs()).append("\n(RETURN ").append(n.e.accept(this)).append(")))");
		--tabs;
		return b.toString();
	}

	public String visit(Formal n) {
		return "(" + n.t.accept(this) + " " + n.i.accept(this) + ")";
	}

	public String visit(IntArrayType n) {
		return "INT[]";
	}

	public String visit(BooleanType n) {
		return "BOOLEAN";
	}

	public String visit(IntegerType n) {
		return "INT";
	}

	public String visit(StringType n) {
		return "STRING";
	}

	public String visit(IdentifierType n) {
		return n.i.accept(this);
	}

	public String visit(Block n) {
		StringBuilder b = new StringBuilder("(BLOCK");
		n.sl.list.forEach(s -> b.append(s.accept(this)).append("\n"));
		return b.append(")").toString();
	}

	public String visit(If n) {
		StringBuilder b = new StringBuilder(tabs()).append("(IF ").append(n.e.accept(this)).append("\n");
		++tabs;
		b.append(tabs()).append(n.s1.accept(this)).append("\n").append(tabs()).append((n.s2 == null ? "" : n.s2.accept
				(this))).append("\n");
		--tabs;
		b.append(tabs()).append(")");
		return b.toString();
	}

	public String visit(While n) {
		return "(WHILE " + n.e.accept(this) + "\n\t" + n.s.accept(this) + "\n)";
	}

	public String visit(Print n) {
		return "(PRINT " + n.e.accept(this) + ")";
	}

	public String visit(Assign n) {
		return "(EQSIGN " + n.i.accept(this) + " " + n.e.accept(this) + ")";
	}

	public String visit(ArrayAssign n) {
		return "(EQSIGN " + n.i.accept(this) + "[" + n.e1.accept(this) + "] " + " " + n.e2.accept(this) + ")";
	}

	public String visit(And n) {
		return "(&& " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Or n) {
		return "(|| " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(LessThan n) {
		return "(< " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Equals n) {
		return "(== " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Plus n) {
		return "(+ " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Minus n) {
		return "(- " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Times n) {
		return "(* " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(Divide n) {
		return "(/ " + n.e1.accept(this) + " " + n.e2.accept(this) + ")";
	}

	public String visit(ArrayLookup n) {
		return "(ARRAY-LOOKUP " + n.e1.accept(this) + "[" + n.e2.accept(this) + "]";
	}

	public String visit(ArrayLength n) {
		return "(LENGTH " + n.e.accept(this) + ")";
	}

	public String visit(Call n) {
		StringBuilder b = new StringBuilder("(DOT ").append(n.e.accept(this)).append(" (FUN-CALL");
		n.el.list.forEach(f -> b.append(" ").append(f.accept(this)));
		return b.append("))").toString();
	}

	public String visit(IntegerLiteral n) {
		return "(INTLIT " + n.i + ")";
	}

	public String visit(StringLiteral n) {
		return "(STRINGLIT " + n.val + ")";
	}

	public String visit(True n) {
		return "TRUE";
	}

	public String visit(False n) {
		return "FALSE";
	}

	public String visit(IdentifierExp n) {
		return n.i.accept(this);
	}

	public String visit(This n) {
		return "THIS";
	}

	public String visit(NewArray n) {
		return "(NEW-INT[] " + n.e.accept(this) + ")";
	}

	public String visit(NewObject n) {
		return "(NEW-OBJECT " + n.i.accept(this) + ")";
	}

	public String visit(Not n) {
		return "(NOT " + n.e.accept(this) + ")";
	}

	public String visit(Identifier n) {
		return "(ID " + n.s + ")";
	}
}
