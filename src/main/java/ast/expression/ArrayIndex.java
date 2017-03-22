package ast.expression;

import ast.Tree;
import ast.Visitor;

public class ArrayIndex extends Tree implements Assignable, Expression {
	public Expression array, index;

	public ArrayIndex(Expression array, Expression index) {
		this.array = array;
		this.index = index;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
