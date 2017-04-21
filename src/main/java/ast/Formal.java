package ast;

import ast.type.Type;

public class Formal extends Decl {

	public Formal(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
