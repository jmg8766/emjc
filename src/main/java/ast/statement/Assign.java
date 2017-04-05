package ast.statement;

import ast.Identifier;
import ast.expression.Exp;

public class Assign extends Statement {
	public Identifier i;
	public Exp e;

	public Assign(Identifier i, Exp e) {
		this.i = i;
		this.e = e;
	}
}
