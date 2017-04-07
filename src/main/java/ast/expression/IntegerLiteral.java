package ast.expression;

import ast.Visitor;

public class IntegerLiteral extends Exp {
	public int i;

	public IntegerLiteral(int i) {
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
