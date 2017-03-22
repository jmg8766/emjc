package ast.expression;

import ast.Tree;
import ast.Visitor;

public class Equals extends Tree implements Expression {
	public Expression lhs, rhs;

	public Equals(Expression lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
