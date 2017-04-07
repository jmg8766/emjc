package ast;

import ast.type.Type;

public class Formal extends Decl {
	public Type t;

	public Formal(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
