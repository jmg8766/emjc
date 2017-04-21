package ast.type;

import ast.ClassDeclaration.ClassDecl;
import ast.Identifier;
import ast.Visitor;

import java.util.HashMap;

public class IdentifierType extends Type {

	public Identifier i;

	public ClassDecl decl;

	private static HashMap<Identifier, IdentifierType> instances = new HashMap<>();

	private IdentifierType(Identifier i) {
		this.i = i;
	}

	public static IdentifierType getInstance(Identifier i) {
		return instances.computeIfAbsent(i, id -> new IdentifierType(id));
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return i.s;
	}
}
