package ast.Statement;

import ast.Visitor;

import java.util.List;

public class Block extends Statement {
    public List<Statement> statements;

    public Block(List<Statement> body) {
        this.statements = body;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}