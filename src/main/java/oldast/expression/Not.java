package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class Not extends Tree implements Expression {

	public Expression expr;

	public Not(int row, int col, Expression expr) {
		this.row = row;
		this.col = col;
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}