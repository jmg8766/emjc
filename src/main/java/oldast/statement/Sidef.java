package oldast.statement;

import oldast.Tree;
import oldast.Visitor;
import oldast.expression.Expression;

public class Sidef extends Tree implements Statement {
    public Expression expression;

    public Sidef(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
