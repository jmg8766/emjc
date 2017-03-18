package ast.Expression;

import ast.Statement.Statement;
import ast.Type.String;
import ast.Type.Type;
import ast.Statement.VarDeclaration;
import ast.Visitor;

import java.util.ArrayList;

public class Method<T extends Type> extends Expression<T> {
    T returnType;
    String id;
    ArrayList<Var> params;
    ArrayList<VarDeclaration> varDeclarations;
    ArrayList<Statement> statements;
    Expression<T> returnExpression;

    public Method(T returnType, String id, ArrayList<Var> params, ArrayList<VarDeclaration> varDeclarations,
                  ArrayList<Statement> statements, Expression<T> returnExpression) {
        this.returnType = returnType;
        this.id = id;
        this.params = params;
        this.varDeclarations = varDeclarations;
        this.statements = statements;
        this.returnExpression = returnExpression;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
