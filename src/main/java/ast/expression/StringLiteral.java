package ast.expression;

import ast.Tree;
import ast.Visitor;

public class StringLiteral extends Tree implements Expression {
    public java.lang.String val;

    public StringLiteral(int row, int col, java.lang.String val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
