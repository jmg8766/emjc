package ast;

import ast.type.Type;
import ast.expression.ID;

import java.util.ArrayList;

public class TypeIdList extends Tree {
    public ArrayList<Type> types;
    public ArrayList<ID> ids;

    public TypeIdList(ArrayList<Type> types, ArrayList<ID> ids) {
        this.types = types;
        this.ids = ids;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
