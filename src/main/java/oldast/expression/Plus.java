package ast.expression;

import ast.Tree;
import ast.Visitor;

public class Plus extends Tree implements Expression {
    public Expression lhs, rhs;

    public Plus(int row, int col, Expression lhs, Expression rhs) {
        this.row = row;
        this.col = col;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
