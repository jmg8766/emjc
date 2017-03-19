package ast.expression;

import ast.Type.Int;
import ast.Visitor;

public class IntPlus extends Expression<Int> {
    Expression<Int> lhs, rhs;

    public IntPlus(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
