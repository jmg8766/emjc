package ast.expression;

import ast.type.Type;

public class Equals<V extends Type> extends Expression<V> {
    public Expression<V> lhs, rhs;

    public Equals(Expression<V> lhs, Expression<V> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
