package ast.expression;

import ast.Tree;
import ast.Visitor;

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
