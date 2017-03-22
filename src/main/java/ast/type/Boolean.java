package ast.type;

import ast.Tree;
import ast.Visitor;

public class Boolean extends Tree implements Type {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
