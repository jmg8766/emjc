package ast.type;

import ast.Visitor;

public class IntegerType extends Type {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
