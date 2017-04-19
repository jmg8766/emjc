package ast.type;

import ast.Visitor;

public class IntArrayType extends Type {

	private final static IntArrayType instance = new IntArrayType();

	private IntArrayType() {
	}

	public static IntArrayType getInstance() {
		return instance;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return "int[]";
	}
}
