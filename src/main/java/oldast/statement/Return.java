package oldast.statement;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Expression;

public class Return<T extends Tree> extends Tree implements Statement {
	public Expression returnValue;

	public Return(Expression returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
