package ast.expression;

import ast.Visitor;

public class NewArray extends Exp {
	public Exp e;

	public NewArray(Exp e) {
		this.e = e;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
