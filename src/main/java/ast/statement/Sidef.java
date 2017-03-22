package ast.statement;

import ast.Visitor;
import ast.expression.Expression;

public class Sidef extends Statement {
    public Expression expression;

    public Sidef(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
