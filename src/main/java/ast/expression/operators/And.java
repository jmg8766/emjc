package ast.expression.operators;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class And extends Tree implements Expression {
    public Expression lhs;
    public Expression rhs;

    public And(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
