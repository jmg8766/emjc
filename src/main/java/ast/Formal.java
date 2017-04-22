package ast;

import ast.type.Type;

public class Formal extends Decl {
    public String pos;

	public Formal(String pos, Type t, Identifier i) {
		this.pos = pos;
		this.t = t;
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
