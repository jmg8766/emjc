package ast.expression;

import ast.type.Int;

public class LessThan extends Expression<Int> {
    public Expression<Int> lhs;
    public Expression<Int> rhs;

    public LessThan(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
