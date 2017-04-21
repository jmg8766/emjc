package ast.expression;

import ast.Identifier;
import ast.Visitor;
import ast.list.ExpList;

public class Call extends Exp {
	public Exp e;
	public Identifier i;
	public ExpList el;

	public Call(String pos, Exp e, Identifier i, ExpList el) {
		this.pos = pos;
		this.e = e;
		this.i = i;
		this.el = el;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
