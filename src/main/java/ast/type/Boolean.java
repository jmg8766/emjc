package ast.type;

import ast.Visitor;

public class Boolean extends Type {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
