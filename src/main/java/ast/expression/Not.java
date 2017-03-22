package ast.expression;

import ast.Tree;
import ast.Visitor;

public class Not extends Tree implements Expression {

	public Expression expr;

	public Not(Expression expr) {
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}