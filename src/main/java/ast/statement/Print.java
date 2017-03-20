package ast.statement;

import ast.expression.Expression;
import ast.expression.ID;
import ast.type.String;
import ast.Visitor;

public class Print extends Statement {
    public Expression<String> msg;

    public Print(Expression<String> msg) {
        this.msg = msg;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}