package ast.type;

import ast.Tree;
import ast.Visitor;

public class IntArray extends Tree implements Type {

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}