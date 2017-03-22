package ast.expression;

import ast.type.Int;

public class IntPlus extends Expression<Int> {
    public Expression<Int> lhs, rhs;

    public IntPlus(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
