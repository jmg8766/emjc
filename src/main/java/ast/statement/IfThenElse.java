package ast.statement;

import ast.expression.Expression;
import ast.type.Boolean;

public class IfThenElse extends Statement {
    public Expression<Boolean> expr;
    public Statement then;
    public Statement elze;  // we cannot use "else" here since it is a reserved word

    public IfThenElse(Expression<Boolean> expr, Statement then, Statement elze) {
        this.expr = expr;
        this.then = then;
        this.elze = elze;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}