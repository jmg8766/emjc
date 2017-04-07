package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class Print extends Statement {
	public Exp e;

	public Print(Exp e) {
		this.e = e;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
