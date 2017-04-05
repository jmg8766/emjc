package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class This extends Tree implements Expression {

	public This(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
