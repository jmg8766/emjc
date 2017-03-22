package ast.expression.operators;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class Or extends Tree implements Expression {
	public Expression lhs, rhs;

	public Or(int row, int col, Expression lhs, Expression rhs) {
		this.row = row;
		this.col = col;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}