package ast.expression;

import ast.Visitor;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(String pos, Exp e) {
		this.pos = pos;
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
