package ast.expression;

import ast.Visitor;
import ast.type.IntegerType;

public class IntegerLiteral extends Exp {
	public int i;

	public IntegerLiteral(String pos, int i) {
		this.pos = pos;
		this.i = i;
		this.t = IntegerType.getInstance();
	}

	public int hashCode() {
		return this.i;
	}

	public boolean equals(Object obj){
		if(obj instanceof IntegerLiteral && ((IntegerLiteral)obj).i == this.i)
			return true;
		else
			return false;

	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString() {
		return Integer.toString(this.i);
	}
}
