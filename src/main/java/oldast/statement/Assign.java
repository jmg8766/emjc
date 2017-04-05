package oldast.statement;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Assignable;
import oldast.expression.Expression;

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