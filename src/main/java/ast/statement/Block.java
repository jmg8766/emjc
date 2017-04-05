package ast.statement;

import ast.list.StatementList;

public class Block extends Statement {
	public StatementList sl;

	public Block(StatementList sl) {
		this.sl = sl;
	}
}
