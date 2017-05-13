package ast.expression;

import ast.Identifier;
import ast.Visitor;
import ast.list.ExpList;

public class Call extends Exp {
	public Exp e;
	public Identifier i;
	public ExpList el;

	public Call(String pos, Exp e, Identifier i, ExpList el) {
		this.pos = pos;
		this.e = e;
		this.i = i;
		this.el = el;
	}

	public int hashCode(){
		return this.e.hashCode()+this.i.hashCode()+this.el.hashCode();
	}

	public boolean equals(Object obj){
		if(obj instanceof Call && (((Call) obj).e.equals(this.e)) && ((Call) obj).i.equals(this.i) && (((Call) obj).el.equals(el)))
				return true;
		return false;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
