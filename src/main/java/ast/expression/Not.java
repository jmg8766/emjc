package ast.expression;

import ast.Visitor;

public class Not extends Exp {
	public Exp e;

	public Not(Exp e) {
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
