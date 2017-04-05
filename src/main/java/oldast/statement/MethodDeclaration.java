package oldast.statement;

import oldast.*;
import oldast.type.Type;

import java.util.ArrayList;

public class MethodDeclaration extends Tree implements Statement, Binding {

	public Type returnType;
	public ID id;
	public TypeIdList params;
	public Block body;

	public MethodDeclaration(Type returnType, ID id, TypeIdList params, ArrayList<VarDeclaration> varDeclarations,
	                         ArrayList<Statement> statements, Return returnExpr) {
		this.returnType = returnType;
		this.id = id;
		this.params = params;
		body = new Block(new ArrayList<Statement>());
		body.statements.addAll(varDeclarations);
		body.statements.addAll(statements);
		body.statements.add(returnExpr);
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
