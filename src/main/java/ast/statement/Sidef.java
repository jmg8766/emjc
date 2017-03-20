package ast.statement;

import ast.expression.Expression;
import ast.Visitor;

public class Sidef extends Statement {
    Expression expression;

    public Sidef(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
