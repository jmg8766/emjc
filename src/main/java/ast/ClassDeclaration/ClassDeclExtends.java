package ast.ClassDeclaration;

import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

public class ClassDeclExtends extends ClassDecl {
	public Identifier parent;

	public ClassDeclExtends(String pos, Identifier i, Identifier parent, VarDeclList v, MethodDeclList m) {
		super(pos, i, v, m);
		this.parent = parent;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		 return v.visit(this);
	}
}
