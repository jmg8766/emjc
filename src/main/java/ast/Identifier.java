package ast;

public class Identifier {
	public Decl b;
	public String s;

	public Identifier(String s) {
		this.s = s;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
