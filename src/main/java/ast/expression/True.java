package ast.expression;

import ast.Visitor;

public class True extends Exp {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
