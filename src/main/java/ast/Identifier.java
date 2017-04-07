package ast;

public class Identifier {
	public String position;
	public Decl b;
	public String s;

	public Identifier(String position, String s) {
		this.position = position; this.s = s;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
