package ast;

public abstract class Tree {

	public int row, col;

	public abstract <R> R accept(Visitor<R> v);          
}