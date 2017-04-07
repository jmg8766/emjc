package ast.type;

import ast.Visitor;

public abstract class Type {
	public abstract <R> R accept(Visitor<R> v);
}
