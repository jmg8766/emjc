package ast;

import ast.expression.Exp;
import ast.list.FormalList;
import ast.list.StatementList;
import ast.list.VarDeclList;
import ast.type.Type;

public class MethodDecl implements Binding {
	public Type t;
	public Identifier i;
	public FormalList fl;
	public VarDeclList vl;
	public StatementList sl;
	public Exp e;

	public MethodDecl(Type t, Identifier i, FormalList fl, VarDeclList vl, StatementList sl, Exp e) {
		this.t = t;
		this.i = i;
		this.fl = fl;
		this.vl = vl;
		this.sl = sl;
		this.e = e;
	}

}
