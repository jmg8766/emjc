package ast;

public abstract class Decl {
	public Identifier i;

	public abstract void accept(Visitor v);
}
