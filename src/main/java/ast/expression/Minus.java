package ast.expression;

import ast.Type.Int;
import ast.Visitor;

public class Minus extends Expression<Int> {
    public Expression<Int> lhs, rhs;

    public Minus(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
