package ast.statement;

import ast.Tree;
import ast.Visitor;

public abstract class Statement extends Tree{
	public String pos = "unsetPos";

	public abstract <R> R accept(Visitor<R> v);
}
