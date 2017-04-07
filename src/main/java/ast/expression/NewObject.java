package ast.expression;

import ast.Identifier;
import ast.Visitor;

public class NewObject extends Exp {
	public Identifier i;

	public NewObject(Identifier i) {
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
