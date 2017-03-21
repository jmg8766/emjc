package ast.statement;

import ast.expression.ID;
import ast.type.String;
import ast.type.Type;
import ast.Visitor;

public class VarDeclaration extends Statement {
    public Type type;
    public ID id;

    public VarDeclaration(Type type, ID id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
