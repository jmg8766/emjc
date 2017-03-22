package ast.expression;

import ast.Tree;
import ast.Visitor;

public class LessThan extends Tree implements Expression {
	public Expression lhs, rhs;

	public LessThan(Expression lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
