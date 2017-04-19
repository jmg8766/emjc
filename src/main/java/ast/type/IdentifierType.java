package ast.type;

import ast.Identifier;
import ast.Visitor;

import java.util.HashMap;
import java.util.LinkedList;

public class IdentifierType extends Type {

//	public Identifier i;
	public LinkedList<Identifier> types = new LinkedList<>();

	private static HashMap<Identifier, IdentifierType> instances = new HashMap<>();

	private IdentifierType(Identifier i) {
		types.addFirst(i);
//		this.i = i;
	}

	public static IdentifierType getInstance(Identifier i) {
		return instances.computeIfAbsent(i, id -> new IdentifierType(id));
	}


	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
