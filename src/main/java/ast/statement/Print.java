package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class Print extends Statement {
	public String pos;
	public Exp e;

	public Print(String pos, Exp e) {
		this.pos = pos;
		this.e = e;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
