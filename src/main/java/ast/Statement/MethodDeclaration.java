package ast.Statement;

import ast.expression.Expression;
import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class MethodDeclaration<T extends Type> extends Statement {
    T returnType;
    String id;
    ArrayList<Type> paramTypes;
    ArrayList<String> paramIds;
    ArrayList<VarDeclaration> varDeclarations;
    ArrayList<Statement> statements;
    Expression<T> returnExpression;

    public MethodDeclaration(T returnType, String id, ArrayList<Type> paramTypes, ArrayList<String> paramIds,
                             ArrayList<VarDeclaration> varDeclarations, ArrayList<Statement> statements,
                             Expression<T> returnExpression) {
        this.returnType = returnType;
        this.id = id;
        this.paramTypes = paramTypes;
        this.paramIds = paramIds;
        this.varDeclarations = varDeclarations;
        this.statements = statements;
        this.returnExpression = returnExpression;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
