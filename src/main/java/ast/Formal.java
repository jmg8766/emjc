package ast;

import ast.type.Type;

public class Formal implements Binding {
	public Type t;
	public Identifier i;

	public Formal(Type t, Identifier i) {
		this.t = t;
		this.i = i;
	}

}
