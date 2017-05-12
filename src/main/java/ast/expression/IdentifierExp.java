package ast.expression;

import ast.Identifier;
import ast.Visitor;

public class IdentifierExp extends Exp {
	public Identifier i;

	public IdentifierExp(String pos, Identifier i) {
		this.pos = pos;
		this.i = i;
	}

	public int hashCode(){return this.i.hashCode();}

	public boolean equals(Object obj) {
		if(obj instanceof IdentifierExp && i.equals(((IdentifierExp)obj).i))
			return true;
		return false;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString(){
		return this.i.s;
	}
}
