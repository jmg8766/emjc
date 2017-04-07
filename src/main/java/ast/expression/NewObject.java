package ast.expression;

import ast.Identifier;
import ast.Visitor;

public class NewObject extends Exp {
	public Identifier i;

	public NewObject(Identifier i) {
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
