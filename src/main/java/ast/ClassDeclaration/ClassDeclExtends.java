package ast.ClassDeclaration;

import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

public class ClassDeclExtends extends ClassDecl {
	public Identifier parent;

	public ClassDeclExtends(Identifier i, Identifier parent, VarDeclList v, MethodDeclList m) {
		super(i, v, m);
		this.parent = parent;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
