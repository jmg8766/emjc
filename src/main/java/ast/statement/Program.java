package ast.statement;

import ast.Tree;
import ast.Visitor;

import java.util.ArrayList;

public class Program extends Tree {
    public MainClassDeclaration main;
    public ArrayList<ClassDeclaration> classDeclarations;

    public Program(MainClassDeclaration main, ArrayList<ClassDeclaration> classDeclarations) {
        this.main = main;
        this.classDeclarations = classDeclarations;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
