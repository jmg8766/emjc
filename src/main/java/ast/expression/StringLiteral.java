package ast.expression;

import ast.Visitor;

public class StringLiteral extends Exp {
	public String val;

	public StringLiteral(String pos, String val) {
		this.pos = pos;
		this.val = val;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
