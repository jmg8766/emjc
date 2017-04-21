package ast.expression;

import ast.Visitor;

public class NewArray extends Exp {
	public Exp e;

	public NewArray(String pos, Exp e) {
		this.pos = pos;
		this.e = e;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
