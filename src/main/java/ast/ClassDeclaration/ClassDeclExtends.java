package ast.ClassDeclaration;

import ast.Binding;
import ast.Identifier;
import ast.VarDecl;
import ast.Visitor;
import ast.list.MethodDeclList;
import ast.list.VarDeclList;
import symbol.Table;

import java.util.Vector;

public class ClassDeclExtends extends ClassDecl {
	public Identifier parent;

	public ClassDeclExtends(Identifier i, Identifier parent, VarDeclList v, MethodDeclList m) {
		super(i, v, m);
		this.parent = parent;
	}

	public VarDeclList getVars(Table<Binding> t) {
		if(!t.contains(parent.s)); //error
		else {
			Vector<VarDecl> parentV = ((ClassDecl) t.get(parent.s)).getVars().list;
			v.list.addAll(parentV);
		}
		return v;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
