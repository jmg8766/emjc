package ast.expression;

import ast.Visitor;

public class True extends Exp {
    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
