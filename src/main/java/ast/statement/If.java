package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class If extends Statement {
	public Exp e;
	public Statement s1;
	public Statement s2;

	public If(Exp e, Statement s1, Statement s2) {
		this.e = e;
		this.s1 = s1;
		this.s2 = s2;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
