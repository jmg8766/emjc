package ast.expression;

import ast.Visitor;

public interface Expression {
	<R> R accept(Visitor<R> v);
}
