package ast.expression;

import ast.Visitor;

public class False extends Exp {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
