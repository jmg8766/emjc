package ast.expression;

import ast.Visitor;

public class Sidef extends Exp {
    public String pos;
    public Exp e;

    public Sidef(String pos, Exp e) {
        this.pos = pos;
        this.e = e;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
