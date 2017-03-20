package ast.statement;

import ast.expression.Expression;
import ast.expression.ID;
import ast.type.Type;
import ast.Visitor;

public class Assign<T extends Type> extends Statement {
    public ID id;
    public Expression<T> expr;

    public Assign(ID id, Expression<T> expr) {
        this.id = id;
        this.expr = expr;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}