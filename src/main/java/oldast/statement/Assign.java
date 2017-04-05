package ast.statement;

import ast.Tree;
import ast.Visitor;
import ast.expression.Assignable;
import ast.expression.Expression;

public class Assign extends Tree implements Statement {
	public Assignable lhs;
	public Expression rhs;

	public Assign(Assignable lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}