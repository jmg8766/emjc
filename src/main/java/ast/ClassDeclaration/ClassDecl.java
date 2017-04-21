package ast.ClassDeclaration;

import ast.Decl;
import ast.Identifier;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;

public abstract class ClassDecl extends Decl {

    public String pos;
	public VarDeclList vl;
	public MethodDeclList ml;

	public ClassDecl(String pos, Identifier i, VarDeclList v, MethodDeclList m) {
		this.pos = pos;
		this.i = i;
		this.vl = v;
		this.ml = m;
	}

	public abstract <R> R accept(Visitor<R> v);
}
