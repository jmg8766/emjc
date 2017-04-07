package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class Print extends Statement {
	public Exp e;

	public Print(Exp e) {
		this.e = e;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
