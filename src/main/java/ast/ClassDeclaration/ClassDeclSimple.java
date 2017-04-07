package ast.ClassDeclaration;

import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

public class ClassDeclSimple extends ClassDecl {

	public ClassDeclSimple(Identifier i, VarDeclList v, MethodDeclList m) {
		super(i, v, m);
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
