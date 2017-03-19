package ast.expression;

import ast.Type.Int;
import ast.Visitor;

public class Times extends Expression<Int> {
    public Expression<Int> lhs;
    public Expression<Int> rhs;

    public Times(Expression<Int> lhs, Expression<Int> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
