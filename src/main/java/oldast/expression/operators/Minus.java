package ast.expression.operators;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class Minus extends Tree implements Expression {
	public Expression lhs, rhs;

	public Minus(int row, int col, Expression lhs, Expression rhs) {
		this.row = row;
		this.col = col;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
