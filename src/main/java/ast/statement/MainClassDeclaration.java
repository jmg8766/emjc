package ast.statement;

import ast.Visitor;
import ast.expression.ID;

public class MainClassDeclaration extends Statement {
    public ID id, args;
    public Statement body;

    public MainClassDeclaration(ID id, ID args, Statement body) {
        this.id = id;
        this.args = args;
        this.body = body;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
