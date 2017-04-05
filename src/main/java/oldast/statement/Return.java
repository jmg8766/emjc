package ast.statement;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

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
