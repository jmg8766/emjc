package ast.type;

import ast.Visitor;

public abstract class Type {
	public abstract void accept(Visitor v);
}
