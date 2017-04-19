package ast.type;

import ast.Visitor;

public class BooleanType extends Type {

	private static final BooleanType instance = new BooleanType();

	private BooleanType() {}

	public static BooleanType getInstance() {
		return instance;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
