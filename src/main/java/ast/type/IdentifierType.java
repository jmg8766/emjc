package ast.type;

import ast.Identifier;
import ast.Visitor;

public class IdentifierType extends Type {
	public Identifier i;

	public IdentifierType(Identifier i) {
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
