package ast;

import ast.type.Type;

public class VarDecl extends Decl {
	public Type t;

	public VarDecl(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
