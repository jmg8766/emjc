package ast.type;

import ast.Visitor;

public class StringType extends Type {

	public void accept(Visitor v) {
		v.visit(this);
	}
}
