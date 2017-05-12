package ast.expression;

import ast.Visitor;

public class Minus extends Exp {
	public Exp e1, e2;

	public Minus(String pos, Exp e1, Exp e2) {
		this.pos = pos;
		this.e1 = e1;
		this.e2 = e2;
	}

	public int hashCode() {
		return this.e1.hashCode() - this.e2.hashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Minus && (((Minus) obj).e1.equals(this.e1)) && ((Minus) obj).e2.equals(this.e2))
			return true;
		return false;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString(){
		return this.e1 + "-" + this.e2;
	}

}
