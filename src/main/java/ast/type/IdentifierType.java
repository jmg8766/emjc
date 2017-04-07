package ast.type;

import ast.Identifier;
import ast.Visitor;

public class IdentifierType extends Type {
	public Identifier i;

	public IdentifierType(Identifier i) {
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
