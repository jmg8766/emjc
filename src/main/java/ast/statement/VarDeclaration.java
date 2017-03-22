package ast.statement;

import ast.ID;
import ast.Tree;
import ast.Visitor;
import ast.type.Type;

public class VarDeclaration extends Tree implements Statement {
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
