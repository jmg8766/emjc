package ast.expression;

import ast.Visitor;

public class Not extends Exp {
	public Exp e;

	public Not(String pos, Exp e) {
		this.pos = pos;
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
