package ast.expression;

import ast.Identifier;
import ast.list.ExpList;

public class Call extends Exp {
	public Exp e;
	public Identifier i;
	public ExpList el;

	public Call(Exp e, Identifier i, ExpList el) {
		this.e = e;
		this.i = i;
		this.el = el;
	}
}
