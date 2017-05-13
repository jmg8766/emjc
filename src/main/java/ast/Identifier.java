package ast;

import ast.expression.IdentifierExp;

public class Identifier extends Tree{
	public String pos;
	public Decl b;
	public String s;

	public Identifier(String pos, String s) {
		this.pos = pos;
		this.s = s;
	}

	public int hashCode(){
		return s.intern().hashCode();
	}

	public boolean equals(Object obj) {
		if((obj instanceof Identifier && s.equals(((Identifier)obj).s)) || (obj instanceof IdentifierExp && s.equals(((IdentifierExp)obj).i.s)))
			return true;
		return false;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString(){
		return this.s;
	}
}
