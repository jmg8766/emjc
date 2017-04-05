package ast;

import ast.expression.Assignable;
import ast.type.Type;

public class ID extends Tree implements Assignable, Type {
	public Binding binding;
	public String id;

	public ID(int row, int col, String id) {
		this.row = row;
		this.col = col;
		this.id = id;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
