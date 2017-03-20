package ast.statement;

import ast.expression.Expression;
import ast.type.Type;
import ast.Visitor;

public class Assign<T extends Type> extends Statement {
    public Var<T> var;
    public Expression<T> expr;

    public Assign(Var<T> var, Expression<T> expr) {
        this.var = var;
        this.expr = expr;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}