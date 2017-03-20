package ast.type;

import ast.Visitor;

public class String extends Type {
    String val;

    public String(String val) {
        this.val = val;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
