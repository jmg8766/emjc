package ast.expression;

import ast.Visitor;

public abstract class Exp {
	public abstract void accept(Visitor v);
}
