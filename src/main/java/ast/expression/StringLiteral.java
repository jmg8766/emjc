package ast.expression;

import ast.Visitor;

public class StringLiteral extends Exp {
	public String val;

	public StringLiteral(String val) {
		this.val = val;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
