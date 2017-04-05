package oldast.statement;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Expression;

public class Print extends Tree implements Statement {
	public Expression msg;

	public Print(Expression msg) {
		this.msg = msg;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}