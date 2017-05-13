package ast.statement;

import ast.Visitor;
import ast.expression.Exp;

public class If extends Statement {
//	public String pos;
	public Exp e;
	public Statement s1;
	public Statement s2;

	public If(String pos, Exp e, Statement s1, Statement s2) {
		this.pos = pos;
		this.e = e;
		this.s1 = s1;
		this.s2 = s2;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
