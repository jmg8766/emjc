package ast;

public abstract class Tree {

    public int row, col;

    public abstract <R> R accept(Visitor<R> v);

    public void setCol(int col) { this.col = col; }

    public void setRow(int row) { this.row = row; }
}