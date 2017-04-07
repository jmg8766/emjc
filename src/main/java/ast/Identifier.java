package ast;

public class Identifier {
	public Decl b;
	public String s;

	public Identifier(String s) {
		this.s = s;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
