package ast;

public abstract class Decl {
	public Identifier i;

	public abstract <R> R accept(Visitor<R> v);
}
