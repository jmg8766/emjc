package ast.statement;

import ast.expression.Exp;

public class Print extends Statement {
	public Exp e;

	public Print(Exp e) {
		this.e = e;
	}
}
