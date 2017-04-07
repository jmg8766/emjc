package ast.expression;

import ast.Visitor;

public class LessThan extends Exp {
	public Exp e1, e2;

	public LessThan(Exp e1, Exp e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
