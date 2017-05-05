import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import java.util.Vector;


public class TypeAnalysis implements Visitor<Type> {

    public static StringBuffer output;

    private void error(String pos, String error) {
        output.append(pos + " error: " + error + "\n");
    }

    /**
     * Checks if the two elements type checks
     * @param right
     * @param left
     * @return
     */
    private boolean checkSubType(Type right, Type left) {
        if(right == left) return true;
        if (right instanceof IdentifierType &&
                ((IdentifierType)right).superTypes.contains(left)) return true;
//                ((IdentifierType) right).decl != null &&
//                ((IdentifierType) right).decl.parentSet.contains(left)) return true;
        return false;
    }

    @Override
    public Type visit(Program n) {
        output = new StringBuffer();
        n.m.accept(this);
        n.cl.list.forEach(c -> c.accept(this));
        return null;
    }

    @Override
    public Type visit(MainClass n) {
        n.s.accept(this);
        return null;
    }

    @Override
    public Type visit(ClassDeclSimple n) {
        n.ml.list.forEach(m -> m.accept(this));
        return null;
    }

    @Override
    public Type visit(ClassDeclExtends n) {
        n.ml.list.forEach(m -> m.accept(this));
        return null;
    }

    @Override
    public Type visit(VarDecl n) {
        return n.t;
    }

    @Override
    public Type visit(MethodDecl n) {
        n.vl.list.forEach(v -> v.accept(this));
        n.sl.list.forEach(s -> s.accept(this));
        Type t1 = n.e.accept(this);
        if(n.t != t1) error(n.pos, "Expected Return type " + n.t + " does not match the current return type " + t1);
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
        return IdentifierType.getInstance(n.i);
    }

    @Override
    public Type visit(Block n) {
        n.sl.list.forEach(s -> s.accept(this));
        return null;
    }

    @Override
    public Type visit(If n) {
        if( n.e.accept(this) != BooleanType.getInstance()) error(n.pos,"exp has to valuate to boolean " );
        n.s1.accept(this);
        if(n.s2 != null) n.s2.accept(this);
        return null;
    }

    @Override
    public Type visit(While n) {
        if(n.e.accept(this) != BooleanType.getInstance()) error(n.pos, "Expected return type boolean");
        n.s.accept(this);
        return null;
    }

    @Override
    public Type visit(Print n) {
        Type t1 = n.e.accept(this);
        if (t1 != IntegerType.getInstance() && t1 != StringType.getInstance() && t1 != BooleanType.getInstance())
            error(n.pos, "print cannot handle type: " + t1);
        return null;
    }

    @Override
    public Type visit(Assign n) {
        Type t1 = n.e.accept(this);
        if(n.i.b.t != t1 && (n.i.b.t instanceof IdentifierType && !checkSubType(t1, n.i.b.t)))
            error(n.pos, "Right hand side " + t1 + " of Assign must be a subtype of the lefthand side " + n.i.b.t);
        return null;
    }

    @Override
    public Type visit(ArrayAssign n) {
        if (n.e1.accept(this) != IntegerType.getInstance())
            error(n.i.pos, "Index position has to be an integer");
        if (n.e2.accept(this) != IntegerType.getInstance())
            error(n.i.pos, "Cannot assign non-integer to integer array");
        return null;
    }

    @Override
    public Type visit(And n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != BooleanType.getInstance() || t2 != BooleanType.getInstance())
            error(n.pos, "Operator && cannot be applied between types [" + t1 + "] and [" + t2 + "]");
        return (n.t = BooleanType.getInstance());
    }

    @Override
    public Type visit(Or n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != BooleanType.getInstance() || t2 != BooleanType.getInstance())
            error(n.pos, "Operator || cannot be applied between types [" + t1 + "] and [" + t2 + "]");
        return (n.t = BooleanType.getInstance());
    }

    @Override
    public Type visit(LessThan n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if (t1 != IntegerType.getInstance() || t2 != IntegerType.getInstance())
            error(n.pos, "Operator < cannot be applied between types [" + t1 + "] and [" + t2 + "]");
        return (n.t = BooleanType.getInstance());
    }

    @Override
    public Type visit(Equals n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);

        if(t1 != t2 && !(t1 instanceof IdentifierType && t2 instanceof IdentifierType))
            error(n.pos, "Cannot compare types " + t1 + " and " + t2);
        return (n.t = BooleanType.getInstance());
    }

    @Override
    public Type visit(Plus n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        // Checks if the type is a string or a integer
        if(t1 == IntegerType.getInstance() && t1 == t2) return (n.t = t1);
        else if ((t1 == IntegerType.getInstance() || t1 == StringType.getInstance()) &&
                 (t2 == IntegerType.getInstance() || t2 == StringType.getInstance())) {
                return (n.t = StringType.getInstance());
        }
        error(n.pos, "Cannot apply + operator on types: " + t1 + " " + t2);
        return null;
    }

    @Override
    public Type visit(Minus n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if(t1 != t2 || !(t1 instanceof IntegerType)) error(n.pos, "Cannot apply - operators on" + t1 + " " + t2);
        return (n.t = IntegerType.getInstance());
    }

    @Override
    public Type visit(Times n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if(t1 != t2 || !(t1 instanceof IntegerType)) error(n.pos,"Cannot apply * operators on" + t1 + " " + t2);
        return (n.t = IntegerType.getInstance());
    }

    @Override
    public Type visit(Divide n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if(t1 != t2 || !(t1 instanceof IntegerType)) error(n.pos, "Cannot apply / operators on" + t1 + " " + t2);
        return (n.t = IntegerType.getInstance());
    }

    @Override
    public Type visit(ArrayLookup n) {
        Type t1 = n.e1.accept(this);
        Type t2 = n.e2.accept(this);
        if( t1 != IntArrayType.getInstance()) error(n.pos,"Does not support array of type: " + t1);
        if( t2 != IntegerType.getInstance()) error(n.pos, "Index has to be of type integer but is "+ t2);
        return (n.t = IntegerType.getInstance());
    }

    @Override
    public Type visit(ArrayLength n) {
        Type t1 = n.e.accept(this);
        if(t1 != IntArrayType.getInstance())
            error(n.pos,t1 + " does not support length");
        return IntegerType.getInstance();
    }

    @Override
    public Type visit(Call n) {
        Type t1 = n.e.accept(this);
        if(!(t1 instanceof IdentifierType)) {
            error(n.pos, " The identifier " + t1 + " does not refer to a class type");
            return null;
        }
        ClassDecl c = ((IdentifierType)t1).decl;
        if(c == null) return null;
        while(true) {
            for (MethodDecl m : c.ml.list) {
                if (m.i.s.equals(n.i.s)) {
                    Vector<Exp> e = n.el.list;
                    Vector<Formal> f = m.fl.list;
                    if (e.size() != f.size()) break; //Need to check parent class method
                    for (int i = 0; i < n.el.list.size(); i++) {
                        Type e1 = e.get(i).accept(this);
                        if(!checkSubType(e1, f.get(i).t)) {
                            error(n.pos, "expected parameter " + (i+1) + " of [" + m.i.s +
                                    "] method call to be of type or subtype: [" + f.get(i).t + "] instead of [" + e1 + "]");
                            break;
                        }
                    }
                    return n.t = m.t;
                }
            }
            if(c instanceof ClassDeclExtends)
                c = (ClassDecl) ((ClassDeclExtends) c).parent.b;
            else break;
        }
        error(n.pos, "The method: [" + n.i.s +"] is not declared for " + t1);
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
        n.t = n.i.b.t;
        return n.i.b.t;
    }

    @Override
    public Type visit(This n) {
        return n.t;
    }

    @Override
    public Type visit(NewArray n) {
        Type t1 = n.e.accept(this);
        if(t1 != IntegerType.getInstance()) error(n.pos, "Array size cannot be type " + t1);
        return n.t = IntArrayType.getInstance();
    }

    @Override
    public Type visit(NewObject n) {

        return n.t = IdentifierType.getInstance(n.i);
    }

    @Override
    public Type visit(Not n) {
        Type t1 = n.e.accept(this);
        if(t1 != BooleanType.getInstance()) error(n.pos, "Operator ! does not support " + t1);
        return n.t = BooleanType.getInstance();
    }

    @Override
    public Type visit(Identifier n) {
        return n.b.t;
    }
}
