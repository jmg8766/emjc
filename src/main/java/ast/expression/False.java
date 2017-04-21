package ast.expression;

import ast.Visitor;

public class False extends Exp {

    public False(String pos) {
        this.pos = pos;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
