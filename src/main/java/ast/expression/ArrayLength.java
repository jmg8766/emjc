package ast.expression;

import ast.Visitor;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(Exp e) {
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return null;
	}
}
