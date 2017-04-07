package ast.expression;

import ast.Visitor;
import ast.statement.Statement;

public abstract class Exp extends Statement {
	public abstract void accept(Visitor v);
}
