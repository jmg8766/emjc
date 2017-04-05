package ast.statement;

import ast.ID;
import ast.Tree;
import ast.Visitor;

public class MainClassDeclaration extends Tree implements Statement {
	public ID id, args;
	public Statement body;

	public MainClassDeclaration(ID id, ID args, Statement body) {
		this.id = id;
		this.args = args;
		this.body = body;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
