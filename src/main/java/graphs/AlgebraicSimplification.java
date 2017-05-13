package graphs;

import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

// Generates the kill and gen sets - which is used for transformations later
public class AlgebraicSimplification implements Visitor {

    HashMap<Identifier, HashSet<Exp>> expressionsList = new HashMap();

    HashMap<Exp, Identifier> availableExpressions = new HashMap();

    public void removeExpressions(Identifier i) {
        //TODO Remove print
        if(expressionsList.containsKey(i)) {
            expressionsList.get(i).forEach(e -> {
//                System.out.println("removing from: " + i + " :" + e);
                availableExpressions.remove(e);
            });
        }
    }

    public void addExpression(Exp exp, Identifier i) {
        if(!(exp instanceof IntegerLiteral) && !(exp instanceof StringLiteral) && !(exp instanceof IdentifierExp) && !(exp instanceof True) && !(exp instanceof False)  ) { // Skipping Integer and String literals as they don't improve
            availableExpressions.put(exp, i);
            //TODO remove print
                if (expressionsList.containsKey(i))
                    removeExpressions(i); // Remove the variables previous assignments
                else {
//                System.out.println("Adding  " + exp + " type :" + exp.getClass() + " => " + i);
                expressionsList.put(i, new HashSet<>());
                expressionsList.get(i).add(exp);
                }
                return;
        }
        removeExpressions(i);
        expressionsList.put(i, new HashSet<>());
    }

    public IdentifierExp useAvaialable(Exp e) {
        Identifier i = availableExpressions.get(e);
        IdentifierExp avail = new IdentifierExp(i.pos, i);
        avail.t = e.t;
        System.out.println("Using available "+ avail + " for exp " + e);
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
        expressionsList = new HashMap<>();
        availableExpressions = new HashMap<>();
        return null;
    }

    @Override
    public Object visit(ClassDeclExtends n) {
        n.vl.list.forEach(v -> v.accept(this));
        n.ml.list.forEach(m-> m.accept(this));
        expressionsList = new HashMap<>();
        availableExpressions = new HashMap<>();
        return null;
    }

    @Override
    public Object visit(VarDecl n) {
        expressionsList.put(n.i, new HashSet<>());
        return null;
    }

    @Override
    public Object visit(MethodDecl n) {
        HashMap classExpList = new HashMap<>();
        classExpList.putAll(expressionsList);
        HashMap classAvailableExpressions = new HashMap<>();
        classAvailableExpressions.putAll(availableExpressions);

        n.fl.list.forEach(f -> f.accept(this));
        n.sl.list.forEach(s->s.accept(this));

//        System.out.println("=======" + n.i + " " +n.i.pos + "==============");
//        expressionsList.entrySet().forEach(System.out::println);
//        System.out.println("=======================================");
//        availableExpressions.entrySet().forEach(System.out::println);
//        System.out.println("=======================================");

        expressionsList.keySet().retainAll(classExpList.keySet());
//        expressionsList.entrySet().forEach(System.out::println);

//        availableExpressions.keySet().retainAll(classAvailableExpressions.keySet());
//        System.out.println("=======================================");
//
//        availableExpressions.entrySet().forEach(System.out::println);
//        System.out.println("=======================================");

        return n;
    }

    @Override
    public Object visit(Formal n) {
        removeExpressions(n.i);
        expressionsList.put(n.i, new HashSet<>());
        return null;
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
    public Object visit(ast.statement.Block n) {
        return null;
    }

//    public Object visit(Block n) {
        //n.sl.list.forEach(s -> s.accept(this));
        //return n;
//        return null;
//    }

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
        else if(n.e.accept(this) != null) addExpression(n.e, n.i);
        return n;
    }

    @Override
    public Object visit(ArrayAssign n) {

        if(expressionsList.containsKey(n.i)) {
            expressionsList.get(n.i).forEach(e-> {
                if(e.equals(n.e1)) {
                    availableExpressions.remove(e); // TODO THAT Expression maybe bound
                }
            });
        }

        if(availableExpressions.containsKey(n.e2)) n.e2 = useAvaialable(n.e2);
        // Add the new expression
        return null;
    }

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
    public Object visit(LessThan n) {
        if(availableExpressions.containsKey(n.e1)) useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2)) useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    @Override
    public Object visit(Equals n) {
        if(availableExpressions.containsKey(n.e1)) useAvaialable(n.e1);
        else n.e1.accept(this);

        if(availableExpressions.containsKey(n.e2)) useAvaialable(n.e2);
        else n.e2.accept(this);

        return n;
    }

    public ArrayList<Exp> getPlusOperands(Plus n){
        ArrayList<Exp> ret = new ArrayList<>();
        if(n.e1 instanceof Plus) ret.addAll(getPlusOperands(((Plus)n.e1))); else ret.add(n.e1);
        if(n.e2 instanceof Plus) ret.addAll(getPlusOperands(((Plus)n.e2))); else ret.add(n.e2);
        return ret;
    }

    @Override
    public Object visit(Plus n) {
        if(!availableExpressions.containsKey(n.e1) || availableExpressions.containsKey(n.e2)) {
            ArrayList arr = getPlusOperands(n);
            if (arr.size() > 2) {
                Iterator<Exp> itr = arr.iterator();
                Iterator<Exp> itr2 = arr.iterator();
                itr2.next();
                while (itr2.hasNext()) {
                    System.out.println(itr);
                    Exp exp1 = itr.next();
                    Exp exp2 = itr2.next();
                    if (availableExpressions.containsKey(new Plus("", exp1, exp2))) {
                        //TODO use this - skip two
                    }

                }
            }
        }
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

        if(n.e1 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e1).i).add(n); // Attach identifiers to expressions
        if(n.e2 instanceof IdentifierExp) expressionsList.get(((IdentifierExp)n.e2).i).add(n); // Attach identifiers to expressions

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
        if(n.e1 instanceof IdentifierExp && expressionsList.containsKey(((IdentifierExp)n.e1).i)) { expressionsList.get(((IdentifierExp)n.e1).i).add(n);}
        return null;
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
        if(n.e instanceof This) {

        }
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
        return n;
    }


    @Override
    public Object visit(False n) {
        return n;
    }

    @Override
    public Object visit(IdentifierExp n) {return n;}

    @Override
    public Object visit(This n) {
        return n;
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
        if(availableExpressions.containsKey(n.e)) useAvaialable(n);
        else n.e.accept(this);
        return n;
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
