package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class While extends Statement {
	public String pos;
	public Exp e;
	public Statement s;

	public While(String pos, Exp e, Statement s) {
		this.pos = pos;
		this.e = e;
		this.s = s;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
