package ast.statement;

import ast.type.Type;
import ast.expression.Expression;

public class Return<T extends Type> extends Statement {
    public Expression returnValue;
    public Type returnType;

    public Return(Type returnType, Expression returnValue) {
        this.returnType = returnType;
        this.returnValue = returnValue;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
