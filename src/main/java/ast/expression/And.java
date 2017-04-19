package ast.expression;

import ast.Visitor;
import ast.type.Type;

public class And extends Exp {
	public Type t;
	public Exp e1, e2;

	public And(Exp e1, Exp e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
