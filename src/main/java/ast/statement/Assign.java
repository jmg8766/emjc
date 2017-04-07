package ast.statement;

import ast.Identifier;
import ast.Visitor;
import ast.expression.Exp;

public class Assign extends Statement {
	public Identifier i;
	public Exp e;

	public Assign(Identifier i, Exp e) {
		this.i = i;
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
