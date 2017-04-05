package ast.expression.operators;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class LessThan extends Tree implements Expression {
	public Expression lhs, rhs;

	public LessThan(int row, int col, Expression lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
