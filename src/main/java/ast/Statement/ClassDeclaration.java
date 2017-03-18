package ast.Statement;

import ast.Type.String;
import ast.Visitor;

import java.util.ArrayList;

public class ClassDeclaration extends Statement {
    String id;
    String parentId;
    ArrayList<VarDeclaration> varDeclarations;
    ArrayList<MethodDeclaration> methodDeclarations;

    public ClassDeclaration(String id, String parentId, ArrayList<VarDeclaration> varDeclarations,
                            ArrayList<MethodDeclaration> methodDeclarations) {
        this.id = id;
        this.parentId = parentId;
        this.varDeclarations = varDeclarations;
        this.methodDeclarations = methodDeclarations;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
