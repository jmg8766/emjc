package ast.statement;

import ast.type.Type;
import ast.TypeIdList;
import ast.Visitor;
import ast.expression.ID;

import java.util.ArrayList;

public class MethodDeclaration<T extends Type> extends Statement {
	public T returnType;
	public ID id;
	public TypeIdList params;
	public Block body;
	public Return<T> returnExpr;

	public MethodDeclaration(T returnType, ID id, TypeIdList params, ArrayList<VarDeclaration> varDeclarations,
	                         ArrayList<Statement> statements, Return<T> returnExpr) {
		this.returnType = returnType;
		this.id = id;
		this.params = params;
		body = new Block(new ArrayList<Statement>());
		body.statements.addAll(varDeclarations);
		body.statements.addAll(statements);
		this.returnExpr = returnExpr;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
