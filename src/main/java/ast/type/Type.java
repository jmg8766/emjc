package ast.type;

import ast.Visitor;

public abstract class Type {
	public abstract Type accept(Visitor v);
}
