package ast.expression;

import ast.Visitor;

public class IntegerLiteral extends Exp {
	public int i;

	public IntegerLiteral(String pos, int i) {
		this.pos = pos;
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
