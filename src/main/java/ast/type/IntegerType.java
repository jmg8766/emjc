package ast.type;

import ast.Visitor;

public class IntegerType extends PrimitiveType {

    private final static IntegerType instance = new IntegerType();

    private IntegerType() {}

    public static IntegerType getInstance() {
        return instance;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
       return v.visit(this);
    }

    @Override
    public String toString() {
        return "int";
    }
}
