package ast;

import ast.statement.Statement;

public class MainClass implements Binding {
	public Identifier i1, i2;
	public Statement s;

	public MainClass(Identifier i1, Identifier i2, Statement s) {
		this.i1 = i1;
		this.i2 = i2;
		this.s = s;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
