package ast.expression;

import ast.type.Int;
import ast.Visitor;

public class Division extends Expression<Int> {

    public Expression<Int> lhs, rhs;

    public Division(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
