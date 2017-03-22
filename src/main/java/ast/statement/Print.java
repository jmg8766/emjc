package ast.statement;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class Print extends Tree implements Statement {
	public Expression msg;

	public Print(Expression msg) {
		this.msg = msg;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}