package ast.ClassDeclaration;

import ast.Decl;
import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

import java.util.LinkedHashSet;

public abstract class ClassDecl extends Decl {

	public LinkedHashSet parentSet;
	public VarDeclList vl;
	public MethodDeclList ml;

	public ClassDecl(Identifier i, VarDeclList v, MethodDeclList m) {
		this.i = i;
		this.vl = v;
		this.ml = m;
	}

	public abstract <R> R accept(Visitor<R> v);
}
