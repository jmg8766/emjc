package ast.expression;

import ast.type.String;
import ast.Visitor;

public class StringLiteral extends Expression<String> {
    public String val;

    public StringLiteral(String val) {
        this.val = val;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
