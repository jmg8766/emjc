package ast.type;

import ast.Visitor;

public class BooleanType extends PrimitiveType {

	private static final BooleanType instance = new BooleanType();

	private BooleanType() {}

	public static BooleanType getInstance() {
		return instance;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return "bool";
	}
}
