package ast.expression;

import ast.Visitor;

public class Plus extends Exp {
	public Exp e1, e2;

	public Plus(String pos, Exp e1, Exp e2) {
		this.pos = pos;
		this.e1 = e1;
		this.e2 = e2;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
