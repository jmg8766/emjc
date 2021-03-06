package ast.type;

import ast.ClassDeclaration.ClassDecl;
import ast.Identifier;
import ast.Visitor;

import java.util.HashMap;
import java.util.HashSet;

public class IdentifierType extends Type {

	public HashSet<Type> superTypes;
	public Identifier i;
	public ClassDecl decl;

	private static HashMap<String, IdentifierType> instances = new HashMap<>();

	private IdentifierType(Identifier i) {
		this.i = i;
		superTypes = new HashSet<>();
	}

	public static IdentifierType getInstance(Identifier i) {
		return instances.computeIfAbsent(i.s.intern(), id -> new IdentifierType(i));
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
