package ast.expression;

import ast.Identifier;
import ast.Visitor;

public class IdentifierExp extends Exp {
	public Identifier i;

	public IdentifierExp(Identifier i) {
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
