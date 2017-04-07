package ast;

import ast.type.Type;

public class VarDecl extends Decl {
	public Type t;

	public VarDecl(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
