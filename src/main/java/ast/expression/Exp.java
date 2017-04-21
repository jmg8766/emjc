package ast.expression;

import ast.Visitor;
import ast.statement.Statement;
import ast.type.Type;

public abstract class Exp extends Statement {
	public String pos;
	public Type t;

	public abstract <R> R accept(Visitor<R> v);
}
