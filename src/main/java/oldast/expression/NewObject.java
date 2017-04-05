package oldast.expression;

import oldast.ID;
import oldast.Tree;
import oldast.Visitor;

public class NewObject extends Tree implements Expression {

	public ID className;

	public NewObject(int row, int col, ID className) {
		this.row = row;
		this.col = col;
		this.className = className;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
