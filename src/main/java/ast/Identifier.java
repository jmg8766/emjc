package ast;

public class Identifier {
	public String pos;
	public Decl b;
	public String s;

	public Identifier(String pos, String s) {
		this.pos = pos;
		this.s = s;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
