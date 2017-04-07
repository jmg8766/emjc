package ast.statement;

import ast.Visitor;

public abstract class Statement {

	public abstract <R> R accept(Visitor<R> v);
}
