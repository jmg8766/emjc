package ast.expression;

import ast.Visitor;

public class This extends Exp {
    public String pos;
    public This(String pos) {
        this.pos = pos;
    }
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
