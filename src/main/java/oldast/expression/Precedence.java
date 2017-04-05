package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class Precedence extends Tree implements Expression {
	public Expression expr;

	public Precedence(int row, int col, Expression expr) {
		this.row = row;
		this.col = col;
		this.expr = expr;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
