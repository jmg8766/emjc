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
	public void accept(Visitor v) {
		v.visit(this);
	}
}
