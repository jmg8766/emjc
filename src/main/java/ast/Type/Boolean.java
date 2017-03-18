package ast.Type;

import ast.Type.Type;
import ast.Visitor;

public class Boolean extends Type {
    boolean val;

    public Boolean(boolean val) {
        this.val = val;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
