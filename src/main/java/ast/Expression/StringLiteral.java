package ast.Expression;

import ast.Type.String;
import ast.Visitor;

public class StringLiteral extends Expression<String> {
    String val;

    public StringLiteral(String val) {
        this.val = val;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
