package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class ArrayIndex extends Tree implements Assignable, Expression {
	public Expression array, index;

	public ArrayIndex(int row, int col, Expression array, Expression index) {
		this.row = row;
		this.col = col;
		this.array = array;
		this.index = index;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
