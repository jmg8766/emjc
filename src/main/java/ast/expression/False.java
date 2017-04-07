package ast.expression;

import ast.Visitor;

public class False extends Exp {
    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
