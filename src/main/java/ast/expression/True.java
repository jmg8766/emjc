package ast.expression;

import ast.Visitor;
import ast.type.BooleanType;

public class True extends Exp {

    public True(String pos) {
        this.pos = pos;
        this.t = BooleanType.getInstance();
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
