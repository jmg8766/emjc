package ast.expression;

public class NewArray extends Exp {
	public Exp e;

	public NewArray(Exp e) {
		this.e = e;
	}
}
