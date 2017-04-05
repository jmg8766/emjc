package ast.expression;

import ast.ID;
import ast.Tree;
import ast.Visitor;

import java.util.ArrayList;

public class FunctionCall extends Tree implements Expression {
    public Expression object;
    public ID id;
    public ArrayList<Expression> params;

    public FunctionCall(Expression object, ID id, ArrayList<Expression> params) {
        this.object = object;
        this.id = id;
        this.params = params;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
