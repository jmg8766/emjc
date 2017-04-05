package ast.expression;

import ast.Tree;
import ast.Visitor;

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
