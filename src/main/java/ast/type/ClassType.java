package ast.type;

import ast.Identifier;
import ast.Visitor;

public class ClassType extends Type{

    public Identifier n;

    public ClassType(Identifier n) {
        this.n = n;
    }

    @Override
    public <R> R accept(Visitor<R> v) { return null; } //TODO

    @Override
    public String toString() {
        return "Class:"+n.s;
    }
}
