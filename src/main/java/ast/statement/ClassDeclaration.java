package ast.statement;

import ast.Visitor;
import ast.expression.ID;

import java.util.ArrayList;

public class ClassDeclaration extends Statement {
    public ID id, parent;
    public ArrayList<VarDeclaration> varDeclarations;
    public ArrayList<MethodDeclaration> methodDeclarations;

    public ClassDeclaration(ID id, ID parentId, ArrayList<VarDeclaration> varDeclarations,
                            ArrayList<MethodDeclaration> methodDeclarations) {
        this.id = id;
        this.parent = parent;
        this.varDeclarations = varDeclarations;
        this.methodDeclarations = methodDeclarations;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
