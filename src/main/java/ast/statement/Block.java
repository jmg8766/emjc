package ast.statement;

import ast.Visitor;
import ast.list.StatementList;

public class Block extends Statement {
	public StatementList sl;

	public Block(StatementList sl) {
		this.sl = sl;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
