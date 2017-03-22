package ast.expression.operators;

import ast.Tree;
import ast.Visitor;
import ast.expression.Expression;

public class Division extends Tree implements Expression {

    public Expression lhs, rhs;

    public Division(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
