package ast.statement;

import ast.Visitor;
import ast.expression.Expression;
import ast.type.Boolean;

public class While extends Statement {
    public Expression<Boolean> expr;
    public Statement body;

    public While(Expression<Boolean> expr, Statement body) {
        this.expr = expr;
        this.body = body;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}