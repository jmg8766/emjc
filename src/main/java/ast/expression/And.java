package ast.expression;

import ast.Visitor;

public class And extends Exp {
	public Exp e1, e2;

	public And(Exp e1, Exp e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
