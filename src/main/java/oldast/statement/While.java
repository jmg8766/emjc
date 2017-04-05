package ast.statement;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class While extends Tree implements Statement {
	public Expression expr;
	public Statement body;

	public While(Expression expr, Statement body) {
		this.expr = expr;
		this.body = body;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}