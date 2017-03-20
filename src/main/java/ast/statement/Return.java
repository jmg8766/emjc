package ast.statement;

import ast.type.Type;
import ast.Visitor;
import ast.expression.Expression;

public class Return<T extends Type> extends Statement {
    public Expression<T> returnValue;

    public Return(Expression<T> returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
