package ast.type;

import ast.Visitor;

public class Int extends Type {
    int value;

    public Int(int value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
