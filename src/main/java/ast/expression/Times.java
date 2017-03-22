package ast.expression;

import ast.Tree;
import ast.Visitor;

public class Times extends Tree {
    public Expression lhs, rhs;

    public Times(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
