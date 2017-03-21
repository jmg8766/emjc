package ast.type;

import ast.expression.ID;

public class Class extends Type{
    ID id;

    public Class(ID id) {
        this.id = id;
    }
}
