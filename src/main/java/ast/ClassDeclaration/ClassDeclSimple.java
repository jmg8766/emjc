package ast.ClassDeclaration;

import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;
import ast.type.Type;

public class ClassDeclSimple extends ClassDecl {

	public ClassDeclSimple(String pos, Identifier i, VarDeclList v, MethodDeclList m) {
		super(pos, i, v, m);
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
