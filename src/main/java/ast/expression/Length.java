package ast.expression;
import ast.ID;
import ast.Tree;
import ast.Visitor;

public class Length extends Tree implements Expression{
    public Expression expr;
    public ID id;

    public Length(Expression expr) {
        this.expr = expr;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
