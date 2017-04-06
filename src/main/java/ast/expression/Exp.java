package ast.expression;

import ast.Visitor;

public abstract class Exp {
	public abstract <R> R accept(Visitor<R> v);
}
