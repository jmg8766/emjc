package ast.ClassDeclaration;

import ast.Binding;
import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

public abstract class ClassDecl implements Binding {

	public Identifier i;
	public VarDeclList v;
	public MethodDeclList m;

	public ClassDecl(Identifier i, VarDeclList v, MethodDeclList m) {
		this.i = i;
		this.v = v;
		this.m = m;
	}

	public VarDeclList getVars() {
		return v;
	}

	public MethodDeclList getMethods() {
		return m;
	}

	public abstract <R> R accept(Visitor<R> v);
}
