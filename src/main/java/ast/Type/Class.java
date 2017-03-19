package ast.Type;

import ast.expression.Var;
import ast.expression.Method;
import ast.Visitor;

import java.util.ArrayList;

public class Class extends Type {
    public String id;
    public ArrayList<Var> vars;
    public ArrayList<Method> methods;

    public Class(String id, ArrayList<Var> vars, ArrayList<Method> methods) {
        this.id = id;
        this.vars = vars;
        this.methods = methods;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
