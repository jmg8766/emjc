package ast.type;

import ast.Visitor;
import ast.expression.ID;
import ast.statement.MethodDeclaration;
import ast.statement.VarDeclaration;

import java.util.ArrayList;

public class Class extends Type {
	public ID id, parent;
	public ArrayList<VarDeclaration> varDeclarations;
	public ArrayList<MethodDeclaration> methodDeclarations;

	public Class(ID id, ID parent, ArrayList<VarDeclaration> varDeclarations,
	             ArrayList<MethodDeclaration> methodDeclarations) {
		this.id = id;
		this.parent = parent;
		this.varDeclarations = varDeclarations;
		this.methodDeclarations = methodDeclarations;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
