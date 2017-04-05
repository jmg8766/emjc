package ast.expression;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(Exp e) {
		this.e = e;
	}
}
