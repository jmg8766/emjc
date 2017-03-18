package ast.Expression;

import ast.Type.String;
import ast.Visitor;

public class StringPlus extends Expression<String> {
    public Expression<String> lhs, rhs;

    public StringPlus(Expression<String> lhs, Expression<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
