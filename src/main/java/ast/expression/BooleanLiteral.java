package ast.expression;

import ast.Tree;
import ast.Visitor;

public class BooleanLiteral extends Tree implements Expression {
	public boolean value;

	public BooleanLiteral(int row, int col, boolean value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}


	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
