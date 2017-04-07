package ast.statement;

import ast.Visitor;

public abstract class Statement {

	public abstract void accept(Visitor v);
}
