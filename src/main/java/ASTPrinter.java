import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

public class ASTPrinter implements Visitor<String> {

	public String visit(Program n) {
		StringBuilder b = new StringBuilder(n.m.accept(this));
		n.cl.list.forEach(c -> b.append(c.accept(this)));
		return b.toString();
	}

	public String visit(MainClass n) {
		return new StringBuilder("(MAIN-CLASS ").append(n.i.accept(this)).append("\n")
				.append("(MAIN-MTD-DECLR ").append(n.i2.accept(this)).append("\n")
				.append(n.s.accept(this)).append("\n")
				.append(")").toString();
	}

	public String visit(ClassDeclSimple n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.i.accept(this));
		n.vl.list.forEach(v -> b.append(v.accept(this)));
		n.ml.list.forEach(m -> b.append(m.accept(this)));
		return b.append(")").toString();
	}

	public String visit(ClassDeclExtends n) {
		StringBuilder b = new StringBuilder("(CLASS ").append(n.i.accept(this))
				.append("EXTENDS ").append(n.parent.accept(this));
		n.vl.list.forEach(v -> b.append(v.accept(this)));
		n.ml.list.forEach(m -> b.append(m.accept(this)));
		return b.append(")").toString();
	}

	public String visit(VarDecl n) {
		return new StringBuilder("(VAR-DECL ").append(n.t.accept(this)).append(" ")
				.append(n.i.accept(this)).append(")").toString();
	}

	public String visit(MethodDecl n) {
		StringBuilder b = new StringBuilder("(MTD-DECL ").append(n.t.accept(this)).append(" ")
				.append(n.i.accept(this)).append(" ").append("(TY-ID-LIST ");
		n.vl.list.forEach(f -> b.append(f.accept(this)));
		b.append(")\n");
		n.sl.list.forEach(s -> b.append(s.accept(this)));
		return b.append(")").toString();
	}

	public String visit(Formal n) {
		return new StringBuilder("(").append(n.t.accept(this)).append(" ").append(n.i.accept(this))
				.append(")").toString();
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
		return new StringBuilder("(IF ").append(n.e.accept(this)).append("\n\t").append(n.s1.accept(this))
				.append("\n\t").append(n.s2 == null ? "" : n.s2.accept(this)).append("\n)").toString();
	}

	public String visit(While n) {
		return new StringBuilder("(WHILE ").append(n.e.accept(this)).append("\n\t").append(n.s.accept(this))
				.append("\n)").toString();
	}

	public String visit(Print n) {
		return new StringBuilder("(PRINT ").append(n.e.accept(this)).append(")").toString();
	}

	public String visit(Assign n) {
		return new StringBuilder("(EQSIGN ").append(n.i.accept(this)).append(" ").append(n.e.accept(this))
				.append(")").toString();
	}

	public String visit(ArrayAssign n) {
		return new StringBuilder("(EQSIGN ").append(n.i.accept(this)).append("[").append(n.e1.accept(this))
				.append("] ").append(" ").append(n.e2.accept(this)).append(")").toString();
	}

	public String visit(And n) {
		return new StringBuilder("(&& ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Or n) {
		return new StringBuilder("(|| ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(LessThan n) {
		return new StringBuilder("(< ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Equals n) {
		return new StringBuilder("(== ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Plus n) {
		return new StringBuilder("(+ ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Minus n) {
		return new StringBuilder("(- ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Times n) {
		return new StringBuilder("(* ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(Divide n) {
		return new StringBuilder("(/ ").append(n.e1.accept(this)).append(" ").append(n.e2.accept(this))
				.append(")").toString();
	}

	public String visit(ArrayLookup n) {
		return new StringBuilder("(ARRAY-LOOKUP ").append(n.e1.accept(this)).append("[")
				.append(n.e2.accept(this)).append("]").toString();
	}

	public String visit(ArrayLength n) {
		return new StringBuilder("(LENGTH ").append(n.e.accept(this)).append(")").toString();
	}

	public String visit(Call n) {
		StringBuilder b = new StringBuilder("(DOT ").append(n.e.accept(this)).append(" (FUN-CALL");
		n.el.list.forEach(f -> b.append(" ").append(f.accept(this)));
		return b.append("))").toString();
	}

	public String visit(IntegerLiteral n) {
		return new StringBuilder("(INTLIT ").append(n.i).append(")").toString();
	}

	public String visit(StringLiteral n) {
		return new StringBuilder("(STRINGLIT ").append(n.val).append(")").toString();
	}

	public String visit(True n) {
		return "TRUE";
	}

	public String visit(False n) {
		return "FALSE";
	}

	public String visit(IdentifierExp n) {
		return n.accept(this);
	}

	public String visit(This n) {
		return "THIS";
	}

	public String visit(NewArray n) {
		return new StringBuilder("(NEW-INT[] ").append(n.e.accept(this)).append(")").toString();
	}

	public String visit(NewObject n) {
		return new StringBuilder("(NEW-OBJECT ").append(n.i.accept(this)).append(")").toString();
	}

	public String visit(Not n) {
		return new StringBuilder("(NOT ").append(n.e.accept(this)).append(")").toString();
	}

	public String visit(Identifier n) {
		return new StringBuilder("(ID ").append(n.s).append(")").toString();
	}
}
