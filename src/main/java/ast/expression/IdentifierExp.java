package ast.expression;

import ast.Identifier;
import ast.Visitor;

public class IdentifierExp extends Exp {
	public Identifier i;

	public IdentifierExp(Identifier i) {
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
