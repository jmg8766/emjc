package graphs;

import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Generates the kill and gen sets - which is used for transformations later
public class AlgebraicSimplification implements Visitor {

    HashMap<Identifier, HashSet<Exp>> expressionsList = new HashMap();
    HashMap<Exp, Identifier> availableExpressions = new HashMap();

    public void removeExpressions(Identifier i) {
        //TODO Remove print
        if(expressionsList.containsKey(i)) {
            expressionsList.get(i).forEach(e -> {
                System.out.println("removing from: " + i + " :" + e);
                availableExpressions.remove(e);
            });
        }
    }

    public void addExpression(Exp exp, Identifier i) {

        // if(exp instanceof IntegerLiteral || exp instanceof StringLiteral) { removeExpressions(i); return;}

        availableExpressions.put(exp, i);

        //TODO remove print
        if(expressionsList.containsKey(i))
            removeExpressions(i);
        else {
            System.out.println("Adding Exp: " + exp + " id:" + i);
            expressionsList.put(i, new HashSet<>());
            expressionsList.get(i).add(exp);
        }
        System.out.println("Adding  "+exp + " => " + i);

        return;
    }

    public IdentifierExp useAvaialable(Exp e) {
//        System.out.println("Using available for exp" + e);
        Identifier i = availableExpressions.get(e);
        IdentifierExp avail = new IdentifierExp(i.pos, i);
        avail.t = e.t;
        return avail;
    }

    @Override
    public Object visit(Program n) {
        n.m.accept(this);
        n.cl.list.forEach(c-> c.accept(this));
        return null;
    }

    @Override
    public Object visit(MainClass n) {
        n.s.accept(this);
        return null;
    }

    @Override
    public Object visit(ClassDeclSimple n) {
        n.vl.list.forEach(v -> v.accept(this));
        n.ml.list.forEach(m-> m.accept(this));
        return null;
    }

    @Override
    public Object visit(ClassDeclExtends n) {
        n.vl.list.forEach(v -> v.accept(this));
        n.ml.list.forEach(m-> m.accept(this));
        return null;
    }

    @Override
    public Object visit(VarDecl n) {
//        expressionsList.put(n.i, new HashSet<>());
        return null;
    }

    @Override
    public Object visit(MethodDecl n) {
        //TODO add to entry node
        n.fl.list.forEach(f -> f.accept(this));
        n.vl.list.forEach(v -> v.accept(this));
        n.sl.list.forEach(s->s.accept(this));
        expressionsList.clear();
        availableExpressions.clear();
        return n;
    }

    @Override
    public Object visit(Formal n) {
//        expressionsList.put(n.i, new HashSet<>());
        return n;
    }

    @Override
    public Object visit(IntArrayType n) {
        return null;
    }

    @Override
    public Object visit(BooleanType n) {
        return null;
    }

    @Override
    public Object visit(IntegerType n) {
        return null;
    }

    @Override
    public Object visit(StringType n) {
        return null;
    }

    @Override
    public Object visit(IdentifierType n) {
        return null;
    }

    @Override
    public Object visit(Block n) {
        n.sl.list.forEach(s -> s.accept(this));
        return n;
    }

    @Override
    public Object visit(If n) {

        n.e.accept(this);

        //nTrue TODO Find a better way of doing this
        HashMap<Identifier, HashSet<Exp>> expListBeforeTrue = new HashMap<>();
        expListBeforeTrue.putAll(expressionsList);
        HashMap<Exp, Identifier> availListBeforeTrue = new HashMap();
        availListBeforeTrue.putAll(availableExpressions);


        n.s1.accept(this);

        HashMap<Identifier, HashSet<Exp>> expListAfterTrue = new HashMap<>();
        expListAfterTrue.putAll(expressionsList);
        HashMap<Exp, Identifier> availListAfterTrue = new HashMap<>();
        availListAfterTrue.putAll(availableExpressions);

        //nFalse
        if(n.s2 != null) {
            expressionsList = expListBeforeTrue;
            availableExpressions = availListBeforeTrue;
            n.s2.accept(this);
        }

        expressionsList.keySet().retainAll(expListAfterTrue.keySet());
        availableExpressions.keySet().retainAll(availListAfterTrue.keySet());

        return n;
        //return startIf.merge();
    }

    @Override
    public Object visit(While n) {

        n.e.accept(this);

        HashMap<Identifier, HashSet<Exp>> expListBeforeTrue = new HashMap<>();
        expListBeforeTrue.putAll(expressionsList);
        HashMap<Exp, Identifier> availListBeforeTrue = new HashMap();
        availListBeforeTrue.putAll(availableExpressions);

        n.s.accept(this);

        expressionsList.keySet().retainAll(expListBeforeTrue.keySet());
        availableExpressions.keySet().retainAll(availListBeforeTrue.keySet());

        return null;
    }

    @Override
    public Object visit(Print n) {
        if(availableExpressions.containsKey(n.e)) n.e = useAvaialable(n.e);
        else n.e.accept(this);
        return n;
    }

    @Override
    public Object visit(Assign n) {
        // Use available expression
        if(availableExpressions.containsKey(n.e)) n.e = useAvaialable(n.e);
        // Add the new expression
        else { if(n.e.accept(this) != null) addExpression(n.e, n.i); }
        return n;
    }

    @Override
    public Object visit(ArrayAssign n) {
        removeExpressions(n.i);
        // TODO Handle array access
        if(availableExpressions.containsKey(n.e2)) n.e2 = useAvaialable(n.e2);
        // Add the new expression
        else { if(n.e2.accept(this) != null) addExpression(n.e2, n.i); }
        return n;
    }

    //TODO And Or short - circuit
    @Override
    public Object visit(And n) {
        HashMap<Identifier, HashSet<Exp>> expListExp1 = new HashMap<>();
        expListExp1.putAll(expressionsList);
        HashMap<Exp, Identifier> availListExp1 = new HashMap();
        availListExp1.putAll(availableExpressions);


        n.e1.accept(this);

        HashMap<Identifier, HashSet<Exp>> expListExp2 = new HashMap<>();
        expListExp2.putAll(expressionsList);
        HashMap<Exp, Identifier> availListExp2= new HashMap<>();
        availListExp2.putAll(availableExpressions);

        //nFalse
        if(n.e2 != null) {
            expressionsList = expListExp1;
            availableExpressions = availListExp1;
            n.e2.accept(this);
        }

        expressionsList.keySet().retainAll(expListExp2.keySet());
        availableExpressions.keySet().retainAll(availListExp2.keySet());

        return n;
    }

    @Override
    public Object visit(Or n) {
        //TODO
        return null;
    }

    @Override
    public Object visit(LessThan n) {
        //TODO
        return null;
    }

    @Override
    public Object visit(Equals n) {
        //TODO
        return null;
    }

    @Override
    public Object visit(Plus n) {
        if(n.e1 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e1).i).add(n);
        if(n.e2 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e2).i).add(n);

        if(availableExpressions.containsKey(n.e1))
            n.e1 = useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2))
            n.e2 = useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    @Override
    public Object visit(Minus n) {
//        Object minus = new Object();
//        if(n.e1.t instanceof IntegerType && n.e2.t instanceof IntegerType)
//                return minus;

        if(n.e1 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e1).i).add(n);
        else if(n.e1 instanceof StringLiteral || n.e1 instanceof IntegerLiteral)
            System.out.println();

        if(n.e2 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e2).i).add(n);



        if(availableExpressions.containsKey(n.e1))
            n.e1 = useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2))
            n.e2 = useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    @Override
    public Object visit(Times n) {
        if(n.e1 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e1).i).add(n);
        if(n.e2 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e2).i).add(n);

        if(availableExpressions.containsKey(n.e1))
            n.e1 = useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2))
            n.e2 = useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    @Override
    public Object visit(Divide n) {
        if(n.e1 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e1).i).add(n);
        if(n.e2 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e2).i).add(n);

        if(availableExpressions.containsKey(n.e1))
            n.e1 = useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2))
            n.e2 = useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    @Override
    public Object visit(ArrayLookup n) {
        //TODO - Optimize
        if(availableExpressions.containsKey(n.e1)) n.e1 = useAvaialable(n.e1);
        else n.e1.accept(this);
        if(availableExpressions.containsKey(n.e2)) n.e2 = useAvaialable(n.e2);
        else n.e2.accept(this);
        return n;
    }

    @Override
    public Object visit(ArrayLength n) {
        if(availableExpressions.containsKey(n.e)) n.e = useAvaialable(n.e);
        else n.e.accept(this);
        return n;
    }

    @Override
    public Object visit(Call n) {
        n.e.accept(this);
        n.el.list.forEach(e->e.accept(this));
        return n;
    }

    @Override
    public Object visit(IntegerLiteral n) {
        return n;
    }

    @Override
    public Object visit(StringLiteral n) {
        return n;
    }

    @Override
    public Object visit(True n) {
        return null;
    }

    @Override
    public Object visit(False n) {
        return null;
    }

    @Override
    public Object visit(IdentifierExp n) {
        return null;
    }

    @Override
    public Object visit(This n) {
        return null;
    }

    @Override
    public Object visit(NewArray n) {
        return n;
    }

    @Override
    public Object visit(NewObject n) {
        return n;
    }

    @Override
    public Object visit(Not n) {
        return null;
    }

    @Override
    public Object visit(Identifier n) {
        return null;
    }

    @Override
    public Object visit(Sidef n) {
        return n.e.accept(this);
    }
}
