package ast.type;

import ast.Visitor;

public class Int extends Type {

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
