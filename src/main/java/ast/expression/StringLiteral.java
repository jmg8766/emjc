package ast.expression;

import ast.Visitor;

public class StringLiteral extends Exp {
	public String val;

	public StringLiteral(String val) {
		this.val = val;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
