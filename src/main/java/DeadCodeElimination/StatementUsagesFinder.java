package DeadCodeElimination;

import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

import java.util.HashSet;
import java.util.stream.Collectors;

public class StatementUsagesFinder implements Visitor<Boolean> {
    static HashSet<Tree> usages = new HashSet<>();
    HashSet<Identifier> varDefinitionsNeeded = new HashSet<>();
    HashSet<MethodDecl> visitedAlready = new HashSet<>();

    @Override
    public Boolean visit(Program n) {
        usages.add(n.m.s); // add the single statement inside main to usages
        n.m.s.accept(this); // visit the contents of that statement

        // add class level varDeclarations to usages
        n.cl.list.forEach(c -> usages.addAll(c.vl.list.stream().filter(varDefinitionsNeeded::contains).collect(Collectors.toList())));

        return true;
    }

    public Boolean visit(MainClass n) {
        return null;
    }

    @Override
    public Boolean visit(ClassDeclSimple n) {
        usages.add(n);
        return null;
    }

    @Override
    public Boolean visit(ClassDeclExtends n) {
        usages.add(n);
        n.parent.b.accept(this);
        return null;
    }

    @Override
    public Boolean visit(VarDecl n) {
        return null;
    }

    @Override
    public Boolean visit(MethodDecl n) {
        if(!visitedAlready.add(n)) return null;
        usages.add(n);
        n.e.accept(this); // and visit it's contents (to add stuff to usages)

        // work backwards through statements adding things that have usages
        for (int i = n.sl.list.size()-1; i >= 0; i--) n.sl.list.get(i).accept(this);

        // then visit all variable declarations removing ones without usages
//        n.vl.list.forEach(v -> v.accept(this));

        // then visit formals TODO: maybe removing the ones that weren't used
        n.fl.list.forEach(f -> f.accept(this));

        return null;
    }

    public Boolean visit(Formal n) {
        varDefinitionsNeeded.remove(n.i);
        return null;
    }

    public Boolean visit(IntArrayType n) {
        return null;
    }
    public Boolean visit(BooleanType n) {
        return null;
    }
    public Boolean visit(IntegerType n) {
        return null;
    }
    public Boolean visit(StringType n) {
        return null;
    }
    public Boolean visit(IdentifierType n) {
        return null;
    }

    @Override
    public Boolean visit(Block n) {
        // visit backwards, looking for anything with usages
        int size = usages.size();
        for (int i = n.sl.list.size()-1; i >= 0; i--) n.sl.list.get(i).accept(this);
        if(usages.size() > size) usages.add(n);
        return null;
    }

    @Override
    public Boolean visit(If n) {
        int i = usages.size();
        // make backups
        HashSet<Identifier> backup = new HashSet<>(); backup.addAll(varDefinitionsNeeded);
        HashSet<Tree> backupUsages = new HashSet<>(); backupUsages.addAll(usages);

        n.s1.accept(this);
        // store results from first branch
        HashSet<Identifier> branch = new HashSet<>(); branch.addAll(varDefinitionsNeeded);
        HashSet<Tree> branchUsages = new HashSet<>(); branchUsages.addAll(usages);


        // restore from backup
        varDefinitionsNeeded.clear(); varDefinitionsNeeded.addAll(backup);
        usages.clear(); usages.addAll(backupUsages);

        if(n.s2 != null) n.s2.accept(this);

        // if neither side of the if statement had usages don't add this if statement to usages
        n.e.accept(this);
        if (usages.size() > i) {
            varDefinitionsNeeded.addAll(branch); // combine varDefinitionsNeeded from both branches
            usages.addAll(branchUsages);
            usages.add(n);
        } else {
            varDefinitionsNeeded = backup;
            usages = backupUsages;
        }
        return null;
    }

    @Override
    public Boolean visit(While n) {
        int i = usages.size();
        // make backups
        HashSet<Identifier> backup = new HashSet<>(); backup.addAll(varDefinitionsNeeded);
        HashSet<Tree> usagesBackup = new HashSet<>(); usagesBackup.addAll(usages);

        n.e.accept(this);
        n.s.accept(this); // check loop body for usages
        if (usages.size() > i) usages.add(n);
        else {
            varDefinitionsNeeded = backup;
            usages = usagesBackup;
        }
        return null;
    }

    @Override
    public Boolean visit(Print n) {
        usages.add(n);
        n.e.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Assign n) {
        if (varDefinitionsNeeded.contains(n.i)) {
            varDefinitionsNeeded.remove(n.i);
            usages.add(n); // add this assignment to usages
            n.e.accept(this);
        }
        return null;
    }

    @Override
    public Boolean visit(ArrayAssign n) {
        if(varDefinitionsNeeded.contains(n.i)) {
            varDefinitionsNeeded.remove(n.i);
            usages.add(n);
            n.e1.accept(this);
            n.e2.accept(this);
        }
        return null;
    }

    @Override
    public Boolean visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Or n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Equals n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Plus n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Divide n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
        return null;
    }

    @Override
    public Boolean visit(ArrayLength n) {
        n.e.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Call n) {
        usages.add(n); // add this Call to the used statements
        n.el.list.forEach(e -> e.accept(this));
        n.e.accept(this); // visit the expression that yields an Object

        n.i.b.accept(this); // visit the method decl being called
        return true;
    }

    public Boolean visit(IntegerLiteral n) {
        return null;
    }
    public Boolean visit(StringLiteral n) {
        return null;
    }
    public Boolean visit(True n) {
        return null;
    }
    public Boolean visit(False n) {
        return null;
    }

    @Override
    public Boolean visit(IdentifierExp n) {
        varDefinitionsNeeded.add(n.i);
        usages.add(n.i.b); // add the declaration of the variable being assigned to usages
        return null;
    }

    @Override
    public Boolean visit(This n) {
        return null;
    }

    @Override
    public Boolean visit(NewArray n) {
        usages.add(n);
        n.e.accept(this);
        return null;
    }

    @Override
    public Boolean visit(NewObject n) {
        n.i.b.accept(this);
        return null;
    }

    public Boolean visit(Not n) {
        n.e.accept(this);
        return null;
    }

    @Override
    public Boolean visit(Identifier n) {
        varDefinitionsNeeded.add(n);
        usages.add(n.b); // add the declaration of the variable being assigned to usages
        return null;
    }

    @Override
    public Boolean visit(Sidef sidef) {
        sidef.e.accept(this);
        return null;
    }
}

