package ast.type;

import ast.Tree;
import ast.Visitor;

public class Boolean extends Tree implements Type {

    public Boolean(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
