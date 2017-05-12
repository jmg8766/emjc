package ast;

import ast.type.Type;

public abstract class Decl extends Tree {
    public Type t;
    public Identifier i;

    public abstract <R> R accept(Visitor<R> v);
}
