package ast.Statement;

import ast.Visitor;

public class MainClassDeclaration extends Statement {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
