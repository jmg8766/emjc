package ast.Expression;

import ast.Type.Boolean;
import ast.Visitor;

public class Not extends Expression<Boolean> {

    public Expression<Boolean> expr;

    public Not(Expression<Boolean> expr) {
        this.expr = expr;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}