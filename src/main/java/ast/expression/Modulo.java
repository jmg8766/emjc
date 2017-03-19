package ast.expression;

import ast.Visitor;

public class Modulo extends Expression {
	public Expression lhs;
	public Expression rhs;
	public Modulo(Expression lhs, Expression rhs) {this.lhs = lhs; this.rhs = rhs;}
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
