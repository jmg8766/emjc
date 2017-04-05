package oldast.expression;

import oldast.Tree;
import oldast.Visitor;

public class IntLiteral extends Tree implements Expression {
	public int value;

	public IntLiteral(int row, int col, int value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
