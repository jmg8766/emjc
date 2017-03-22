package ast.expression;

import ast.Tree;
import ast.Visitor;

public class Precedence extends Tree implements Expression {
	public Expression e;

	public Precedence(int row, int col, Expression e) {
		this.row = row;
		this.col = col;
		this.e = e;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
