package ast.expression;

import ast.Visitor;

import java.lang.reflect.Array;

public class ArrayLookup extends Exp {
	public Exp e1, e2;

	public ArrayLookup(String pos, Exp e1, Exp e2) {
	    this.pos = pos;
		this.e1 = e1;
		this.e2 = e2;
	}

	public int hashCode(){
		return e1.hashCode()+e2.hashCode();
	}

	public boolean equals(Object obj){
		if(obj instanceof ArrayLookup && ((ArrayLookup) obj).e1.equals(e1) && ((ArrayLookup)obj).e2.equals(e2))
			return true;
		return false;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
