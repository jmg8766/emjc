package ast.expression;

import ast.Visitor;

import java.util.ArrayList;

public class FunctionCall extends Expression {
    public Expression parent;
    public ID id;
    public ArrayList<Expression> params;

    public FunctionCall(Expression parent, ID id, ArrayList<Expression> params) {
        this.parent = parent;
        this.id = id;
        this.params = params;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
