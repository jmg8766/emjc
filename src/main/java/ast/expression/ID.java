package ast.expression;

import ast.Visitor;

public class ID extends Expression {
    public String id;

    public ID(String id) {
        this.id = id;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
