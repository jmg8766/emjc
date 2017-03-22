package ast.statement;

import ast.ID;
import ast.Tree;
import ast.Visitor;

import java.util.ArrayList;

public class ClassDeclaration extends Tree implements Statement {
	public ID id, parent;
	public ArrayList<VarDeclaration> varDeclarations;
	public ArrayList<MethodDeclaration> methodDeclarations;

	public ClassDeclaration(ID id, ID parentId, ArrayList<VarDeclaration> varDeclarations,
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
