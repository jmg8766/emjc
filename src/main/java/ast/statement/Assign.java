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

	public void accept(Visitor v) {
		v.visit(this);
	}
}
