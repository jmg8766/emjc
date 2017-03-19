package ast.expression;

import ast.Type.Boolean;
import ast.Visitor;

public class Or extends Expression<Boolean> {
    public Expression<Boolean> lhs;
    public Expression<Boolean> rhs;

    public Or(Expression<Boolean> lhs, Expression<Boolean> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}