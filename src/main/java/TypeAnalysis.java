import ast.*;
import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.list.ExpList;
import ast.list.FormalList;
import ast.statement.*;
import ast.type.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Vector;


public class TypeAnalysis implements Visitor<Type> {

    private void error(java.lang.String error) {
        System.out.println(error);
    }

    private enum types {

        BOOL(BooleanType.getInstance()),
        INT(IntegerType.getInstance()),
        STRING(StringType.getInstance());

        private Type t;

        types(Type t) {
            this.t = t;
        }

        public Type getT() {
            return t;
        }
    }

    /**
     * Checks if the two elements type checks
     * @param r
     * @param l
     * @return
     */
    private boolean checkSubType(Type r, Type l) {
        if (r instanceof PrimitiveType && l instanceof PrimitiveType)
            if(r == l)
                return true;
        else if (r instanceof IdentifierType && l instanceof IdentifierType);
        {   //There was a linked map for parents then we could check instantly for every rhs parent nodes
            if (((ClassDecl) ((IdentifierType) r).decl).parentSet.contains(l)) //TODO
                return true;
        }
        return false;
    }

    @Override
    public Type visit(Program n) {
        n.m.accept(this);
        n.cl.list.forEach(c -> c.accept(this));
        return null;
    }

    @Override
    public Type visit(MainClass n) {
        n.s.accept(this);
        return n.t;
    }

    @Override
    public Type visit(ClassDeclSimple n) {
        n.ml.list.forEach(m -> m.accept(this));
        return null;
    }

    @Override
    public Type visit(ClassDeclExtends n) {
        n.ml.list.forEach(m -> m.accept(this));
        return n.t;
    }

    @Override
    public Type visit(VarDecl n) {
        return n.t;
    }

    @Override
    public Type visit(MethodDecl n) {
        n.vl.list.forEach(v -> v.accept(this));
        n.sl.list.forEach(s -> s.accept(this));
        if (!checkSubType(n.e.accept(this), n.t))
            error("Expected Return type does not match the current return type");
        return n.t;
    }

    @Override
    public Type visit(Formal n) {
        return n.t;
    }

    @Override
    public Type visit(IntArrayType n) {
        return n.getInstance();
    }

    @Override
    public Type visit(BooleanType n) {
        return n.getInstance();
    }

    @Override
    public Type visit(IntegerType n) {
        return n.getInstance();
    }

    @Override
    public Type visit(StringType n) {
        return n.getInstance();
    }

    @Override
    public Type visit(IdentifierType n) {
        return n.i.b.t;
    }

    @Override
    public Type visit(Block n) {
        n.sl.list.forEach(s -> s.accept(this));
        return null;
    }

    @Override
    public Type visit(If n) {
        Type t1 = n.e.accept(this);
        if (t1 != BooleanType.getInstance()) // TODO Needs position
            error("exp has to valuate to boolean " );
        n.s1.accept(this);
        n.s2.accept(this);
        return null;
    }

    @Override
    public Type visit(While n) {
        if (!(n.e.accept(this).equals(BooleanType.getInstance())))
            error("Expected return type boolean");
        n.s.accept(this);
        return null;
    }

    @Override
    public Type visit(Print n) {
        Type t1 = n.e.accept(this);
        if (t1 != IntegerType.getInstance() && t1 != StringType.getInstance() && t1 != BooleanType.getInstance())
            error("print cannot handle type: " + t1);
        return null;
    }

    @Override
    public Type visit(Assign n) {
        if (!checkSubType(n.e.accept(this), n.i.b.t))
            error(n.i.s);
        return null;
    }

    @Override
    public Type visit(ArrayAssign n) {
        if (n.e1.accept(this) != IntegerType.getInstance())
            error(n.i.pos + " Index position has to be an integer");
        if (n.e2.accept(this) != IntegerType.getInstance())
            error(n.i.pos + "Cannot assign non-integer to integer array");
        return IntArrayType.getInstance();
    }

    @Override
    public Type visit(And n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != IntegerType.getInstance() || t2 != IntegerType.getInstance())
            error("Operator && cannot be applied between types " + t1 + " and " + t2);
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(Or n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != IntegerType.getInstance() || t2 != IntegerType.getInstance())
            error("Operator || cannot be applied between types " + t1 + " and " + t2);
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(LessThan n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != IntegerType.getInstance() || t2 != IntegerType.getInstance())
            error("Operator < cannot be applied between types " + t1 + " and " + t2);
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(Equals n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 instanceof PrimitiveType && t2 instanceof PrimitiveType) ;
        if (t1 == t2) return BooleanType.getInstance();
        else if (t1 instanceof IdentifierType && t2 instanceof IdentifierType)
            return BooleanType.getInstance();
        error("Cannot compare types" + t1 + " " + t2);
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(Plus n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 == IntegerType.getInstance() && t2 == IntegerType.getInstance())
            return IntegerType.getInstance();
        else if ((t1 == StringType.getInstance() && t2 == StringType.getInstance()) ||
                (t1 == IntegerType.getInstance() || t2 == IntegerType.getInstance()) &&
                (t1 == StringType.getInstance() || t2 == StringType.getInstance())) {
            n.t = StringType.getInstance();
            return StringType.getInstance();
        }
        error("Cannot apply + operator on types: " + t1 + " " + t2);
        return null; //TODO Will impact assign
    }

    @Override
    public Type visit(Minus n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 == IntegerType.getInstance() && t2 == IntegerType.getInstance()) {
            n.t = IntegerType.getInstance();
            return IntegerType.getInstance();
        }
        error("Cannot apply - operators on" + t1 + " " + t2);
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(Times n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 == IntegerType.getInstance() && t2 == IntegerType.getInstance()) {
            n.t = IntegerType.getInstance();
            return IntegerType.getInstance();
        }
        error("Cannot apply * on operators " + t1 + " " + t2);
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(Divide n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 == IntegerType.getInstance() && t2 == IntegerType.getInstance()) {
            n.t = IntegerType.getInstance();
            return IntegerType.getInstance();
        }
        error("Cannot apply / operator on" + t1 + " " + t2);
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(ArrayLookup n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if( t1 != IntArrayType.getInstance())
            error("Does not support array of type: " + t1);
        if( t2 != IntegerType.getInstance())
            error("Index has to be of type integer but is "+ t2);
        return null;
    }

    @Override
    public Type visit(ArrayLength n) {
        Type t1 = n.e.accept(this);
        if(t1 != IntArrayType.getInstance())
            error(t1 + "does not support length");
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(Call n) {
        Type t1 = n.e.accept(this);
        ClassDecl c;
        if(t1 instanceof IdentifierType)
            c = ((ClassDecl)((IdentifierType)t1).decl);
        else if(t1 instanceof ClassType) {
             c = ((ClassDecl)((ClassType)t1).n.b.i.b);
        } else return null; //TODO
        while(true) {
            for (MethodDecl m : c.ml.list) {
                if (m.i.s.equals(n.i.s)) {
                    Vector<Exp> e = n.el.list;
                    Vector<Formal> f = m.fl.list;
                    if (e.size() != f.size())
                        break; //Need to check parent class method
                    for (int i = 0; i < n.el.list.size(); i++) {
                        Type e1 = e.get(i).accept(this);
                        if( checkSubType(e1, f.get(i).t))
                        //if (e1 == f.get(i).t) //TODO check subtypes
                            continue;
                    }
                    return m.t;
                }
            }
            if(c instanceof ClassDeclExtends)
                c = (ClassDecl) ((ClassDeclExtends) c).parent.b;
            else
                break;
        }
        error("The method" + n.i +"is not declared for " + t1);
        return null;
    }

    @Override
    public Type visit(IntegerLiteral n) {
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(StringLiteral n) {
        return StringType.getInstance();
    }

    @Override
    public Type visit(True n) {
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(False n) {
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(IdentifierExp n) {
        Type t1 = n.i.b.accept(this);
        if(t1 instanceof IdentifierType)
            t1 = IdentifierType.getInstance(n.i.b.i);
            n.t = t1;
        System.out.println("ID: " + n.i.s + " -- " +n.t + n.i.pos);
        return n.t;
    }

    @Override
    public Type visit(This n) {
        return n.t;
    }

    @Override
    public Type visit(NewArray n) {
        Type t1 = n.e.accept(this);
        if(t1 != IntegerType.getInstance())
            error("Array size cannot be type " + t1);
        n.t = t1;
        return IntArrayType.getInstance();
    }

    @Override
    public Type visit(NewObject n) {
        return n.i.b.t;
    }

    @Override
    public Type visit(Not n) {
        Type t1 = n.e.accept(this);
        if(t1 != BooleanType.getInstance())
            error("Operator ! does not support " + t1);
        n.t = t1;
        return BooleanType.getInstance();
    }

    @Override
    public Type visit(Identifier n) {
        System.out.println("ID: " + n.b.i.s + "--" + n.b.t);
        return n.b.t;
    }
}
