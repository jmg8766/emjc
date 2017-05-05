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

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
