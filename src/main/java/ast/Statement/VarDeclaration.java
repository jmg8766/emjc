package ast.Statement;

import ast.Type.String;
import ast.Type.Type;
import ast.Visitor;

public class VarDeclaration<T extends Type> extends Statement {
    public T type;
    public String id;

    public VarDeclaration(T t, String id) {
        this.id = id;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
