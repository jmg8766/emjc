package ast.expression;

import ast.Visitor;

public class Divide extends Exp{
	public Exp e1, e2;

	public Divide(String pos, Exp e1, Exp e2) {
		this.pos = pos;
		this.e1 = e1;
		this.e2 = e2;
	}

	public int hashCode() {
		return this.e1.hashCode() / this.e2.hashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Divide && (((Divide) obj).e1.equals(this.e1)) && ((Divide) obj).e2.equals(this.e2))
			return true;
		return false;
	}

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

	public String toString(){
		return this.e1 + " /" + this.e2;
	}
}
