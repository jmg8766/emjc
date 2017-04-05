package ast;

import ast.list.ClassDeclList;

public class Program {
	public MainClass m;
	public ClassDeclList cl;

	public Program(MainClass m, ClassDeclList cl) {
		this.m = m;
		this.cl = cl;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
