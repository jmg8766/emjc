package DeadCodeElimination;

import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

import java.util.HashSet;
import java.util.stream.Collectors;

public class DeadCodeEliminator implements Visitor<Tree> {
    HashSet<Tree> usages;

    public Tree visit(Program n) {
        new StatementUsagesFinder().visit(n);
        usages = StatementUsagesFinder.usages;

        // remove any unused class declarations
        n.cl.list = n.cl.list.stream().filter(usages::contains).collect(Collectors.toList());

        n.cl.list.forEach(c -> {
            // remove any unused variable declarations
            c.vl.list = c.vl.list.stream().filter(v -> {
                if(usages.contains(v)) return true;
                else {
                    System.out.println("removing class instance variable: " + v.i.s);
                    return false;
                }
            }).collect(Collectors.toList());
            // remove any unused method declarations
            c.ml.list = c.ml.list.stream().filter(m -> {
                if(usages.contains(m)) return true;
                else {
                    System.out.println("removing method: " + m.i.s);
                    return false;
                }
            }).collect(Collectors.toList());
            // remove any unused variable declarations and statements from remaining method declarations
            c.ml.list.forEach(m -> {
                m.vl.list = m.vl.list.stream().filter(v -> {
                    if(usages.contains(v)) return true;
                    else {
                        System.out.println("removing a local var Declaration: " + v);
                        return false;
                    }
                }).collect(Collectors.toList());
                m.sl.list = m.sl.list.stream().filter(s -> {
                    if(usages.contains(s)) return true;
                    else {
                        System.out.println("removing a top level statement: " + s + " pos: " + s.pos);
                        return false;
                    }
                }).collect(Collectors.toList());

                // visit each statement
                m.sl.list.forEach(s -> s.accept(this));
            });
        });
        return n;
    }

    public Tree visit(MainClass n) { return null; }
    public Tree visit(ClassDeclSimple n) { return null; }
    public Tree visit(ClassDeclExtends n) { return null; }
    public Tree visit(VarDecl n) { return null; }
    public Tree visit(MethodDecl n) { return null; }
    public Tree visit(Formal n) { return null; }
    public Tree visit(IntArrayType n) { return null; }
    public Tree visit(BooleanType n) { return null; }
    public Tree visit(IntegerType n) { return null; }
    public Tree visit(StringType n) { return null; }
    public Tree visit(IdentifierType n) {return null;}


    public Tree visit(Block n) {
        n.sl.list = n.sl.list.stream().filter(s -> {
            if(usages.contains(s)) return true;
            else {
                System.out.println("removing a statement inside a block: " + s + " pos: " + s.pos);
                return false;
            }
        }).collect(Collectors.toList());
        return null;
    }

    public Tree visit(If n) {
        n.s1.accept(this);
        if(n.s2 != null) n.s2.accept(this);
        return null;
    }

    public Tree visit(While n) {
        n.s.accept(this);
        return null;
    }

    public Tree visit(Print n) {
        return null;
    }

    public Tree visit(Assign n) {
        return null;
    }

    public Tree visit(ArrayAssign n) {
        return null;
    }

    public Tree visit(And n) { return null; }
    public Tree visit(Or n) { return null;}
    public Tree visit(LessThan n) {return null;}
    public Tree visit(Equals n) { return null; }
    public Tree visit(Plus n) { return null; }
    public Tree visit(Minus n) { return null; }
    public Tree visit(Times n) { return null; }
    public Tree visit(Divide n) { return null; }
    public Tree visit(ArrayLookup n) { return null; }
    public Tree visit(ArrayLength n) { return null; }
    public Tree visit(Call n) { return null; }
    public Tree visit(IntegerLiteral n) { return null; }
    public Tree visit(StringLiteral n) { return null; }
    public Tree visit(True n) { return null; }
    public Tree visit(False n) { return null; }
    public Tree visit(IdentifierExp n) { return null; }
    public Tree visit(This n) { return null; }
    public Tree visit(NewArray n) { return null; }
    public Tree visit(NewObject n) { return null; }
    public Tree visit(Not n) { return null; }
    public Tree visit(Identifier n) { return null;}

    public Tree visit(Sidef sidef) {
        return null;
    }
}

