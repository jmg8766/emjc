package oldast.statement;

import oldast.Tree;
import oldast.Visitor;

import java.util.ArrayList;

public class Block extends Tree implements Statement {
	public ArrayList<Statement> statements;

	public Block(ArrayList<Statement> body) {
		this.statements = body;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}