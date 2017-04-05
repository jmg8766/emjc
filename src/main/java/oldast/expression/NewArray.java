package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class NewArray extends Tree implements Expression {
	public Expression arrayLength;

	public NewArray(int row, int col, Expression arrayLength) {
		this.row = row;
		this.col = col;
		this.arrayLength = arrayLength;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
