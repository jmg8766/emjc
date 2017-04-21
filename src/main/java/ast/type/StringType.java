package ast.type;

import ast.Visitor;

public class StringType extends PrimitiveType {

	private final static StringType instance = new StringType();

	private StringType() {}

	public static StringType getInstance() {
		return instance;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return "String";
	}
}
