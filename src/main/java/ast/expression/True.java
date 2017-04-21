package ast.expression;

import ast.Visitor;

public class True extends Exp {

    public True(String pos) {
        this.pos = pos;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
