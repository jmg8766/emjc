package ast.type;

import ast.Tree;
import ast.Visitor;

public class Int extends Tree implements Type {

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
