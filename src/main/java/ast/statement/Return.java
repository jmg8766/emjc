package ast.statement;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;
import ast.type.Type;

public class Return<T extends Tree> extends Tree implements Statement {
	public Expression returnValue;
	public Type returnType;

	public Return(Type returnType, Expression returnValue) {
		this.returnType = returnType;
		this.returnValue = returnValue;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
