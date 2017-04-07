package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class While extends Statement {
	public Exp e;
	public Statement s;

	public While(Exp e, Statement s) {
		this.e = e;
		this.s = s;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
