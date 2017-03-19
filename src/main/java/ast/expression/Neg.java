package ast.expression;

import ast.Visitor;

public class Neg extends Expression {
	public Expression expr;
	public Neg(Expression expr) {this.expr = expr;}
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
