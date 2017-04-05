package oldast.expression.operators;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Expression;

public class Equals extends Tree implements Expression {
	public Expression lhs, rhs;

	public Equals(int row, int col, Expression lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
