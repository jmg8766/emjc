package ast.expression;

import ast.Visitor;

public class This extends Exp {
	public String pos;

	public This(String pos) {
		this.pos = pos;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
