package ast.expression;

import ast.Tree;
import ast.Visitor;

public class StringPlus extends Tree implements Expression {
	public Expression lhs, rhs;

	public StringPlus(Expression lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
