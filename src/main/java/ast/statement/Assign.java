package ast.statement;

import ast.Identifier;
import ast.Visitor;
import ast.expression.Exp;

public class Assign extends Statement {
	public String pos;
	public Identifier i;
	public Exp e;

	public Assign(String pos, Identifier i, Exp e) {
		this.pos = pos;
		this.i = i;
		this.e = e;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString(){
		return this.i + " = " + this.e;
	}
}
