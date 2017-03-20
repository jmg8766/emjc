package ast.statement;

import ast.expression.ID;
import ast.type.String;
import ast.type.Type;
import ast.Visitor;

public class VarDeclaration<T extends Type> extends Statement {
    public T type;
    public ID id;

    public VarDeclaration(T t, ID id) {
        this.id = id;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
