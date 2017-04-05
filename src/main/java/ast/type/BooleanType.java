package ast.type;

import ast.Visitor;

public class BooleanType extends Type {

	<R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
