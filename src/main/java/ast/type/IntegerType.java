package ast.type;

import ast.Visitor;

public class IntegerType extends Type {
    @Override
    public <R> R accept(Visitor<R> v) {
       return v.visit(this);
    }
}
