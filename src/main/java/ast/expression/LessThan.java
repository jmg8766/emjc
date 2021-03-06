package ast.expression;

import ast.Visitor;

public class LessThan extends Exp {
	public Exp e1, e2;

	public LessThan(String pos, Exp e1, Exp e2) {
		this.pos = pos;
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
