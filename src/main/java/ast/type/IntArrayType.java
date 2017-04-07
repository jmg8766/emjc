package ast.type;

import ast.Visitor;

public class IntArrayType extends Type {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
