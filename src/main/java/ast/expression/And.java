package ast.expression;

import ast.Type.Boolean;
import ast.Visitor;

public class And extends Expression<Boolean> {
    public Expression<Boolean> lhs;
    public Expression<Boolean> rhs;

    public And(Expression<Boolean> lhs, Expression<Boolean> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
