package ast.statement;

import ast.type.String;
import ast.Visitor;

public class Print extends Statement {
    public String msg;
    public String varID;

    public Print(String msg, String varID) {
        this.msg = msg;
        this.varID = varID;
    }

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}