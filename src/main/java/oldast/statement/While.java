package oldast.statement;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Expression;

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