package ast.expression;

import ast.Type.Int;
import ast.Visitor;

public class IntLiteral extends Expression<Int> {
    public int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
