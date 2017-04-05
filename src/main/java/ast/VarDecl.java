package ast;

import ast.type.Type;

public class VarDecl implements Binding {
	public Type t;
	public Identifier i;

	public VarDecl(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}
}
