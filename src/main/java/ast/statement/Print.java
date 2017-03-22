package ast.statement;

import ast.expression.Expression;
import ast.type.String;

public class Print extends Statement {
    public Expression<String> msg;

    public Print(Expression<String> msg) {
        this.msg = msg;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}