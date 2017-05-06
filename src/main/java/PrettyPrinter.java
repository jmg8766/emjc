import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.list.ExpList;
import ast.list.FormalList;
import ast.statement.*;
import ast.type.*;

import java.util.HashMap;
import java.util.Iterator;

public class PrettyPrinter implements Visitor<String> {
    int idCount = 0;
    HashMap<Decl, String> id = new HashMap<>();

    @Override
    public String visit(Program n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.m.accept(this));
        n.cl.list.forEach(c -> sb.append(c.accept(this)));
        System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public String visit(MainClass n) {
        return "class " + n.i.accept(this) + "{\n\t public static void main(String [] "+ n.i2.accept(this )+") {\n" + n.s.accept(this) + "\t}\n}\n";
    }

    @Override
    public String visit(ClassDeclSimple n) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ");
        sb.append(n.i.accept(this));
        sb.append(" {");
        n.vl.list.forEach(v-> {sb.append("\n"); sb.append(v.accept(this));});
        n.ml.list.forEach(m ->{sb.append("\n"); sb.append(m.accept(this));});
        sb.append("\n}\n");
        return sb.toString();
    }

    @Override
    public String visit(ClassDeclExtends n) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ");
        sb.append(n.i.accept(this));
        sb.append(" extends ");
        sb.append(n.parent.accept(this));
        n.parent.b.accept(this); // Not adding Parent variables and methods
        sb.append(" {");
        n.vl.list.forEach(v-> {sb.append("\n\t");sb.append(v.accept(this));});
        n.ml.list.forEach(m ->  {sb.append("\n\t"); sb.append(m.accept(this));});
        sb.append("\n}\n");
        return sb.toString();
    }

    @Override
    public String visit(VarDecl n) {
        return "\t" + n.t.accept(this) + n.i.accept(this) + ";";
    }

    @Override
    public String visit(MethodDecl n) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic ");
        sb.append(n.t.accept(this));
        sb.append(n.i.accept(this));
        sb.append(visit(n.fl));
        n.vl.list.forEach(v-> {sb.append("\n\t"); sb.append(v.accept(this));});
        n.sl.list.forEach(s->{sb.append("\n\t\t"); sb.append(s.accept(this));});
        sb.append("\n\t\treturn ");
        sb.append(n.e.accept(this));
        sb.append("\n\t}");
        return sb.toString();
    }

    public String visit(FormalList l) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Iterator<Formal> itr = l.list.iterator();
        if(itr.hasNext()) {
            itr.next().accept(this);
            while (itr.hasNext()) {
                sb.append(", ");
                itr.next().accept(this);
            }
        }
        sb.append(") {");
        return sb.toString();

    }
    @Override
    public String visit(Formal n) {
        return n.t.accept(this)  + n.i.accept(this);
    }

    @Override
    public String visit(IntArrayType n) {
        return "int[] ";
    }

    @Override
    public String visit(BooleanType n) {
        return "boolean ";
    }

    @Override
    public String visit(IntegerType n) {
        return "int ";
    }

    @Override
    public String visit(StringType n) {
        return "String ";
    }

    @Override
    public String visit(IdentifierType n) {
        return n.i.accept(this);
    }

    @Override
    public String visit(Block n) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        n.sl.list.forEach(s->{sb.append("\n\t\t");s.accept(this);});
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String visit(If n) {
        return "\t\tif(" + n.e.accept(this) + ") \n\t\t\t" + n.s1.accept(this) + " \n\t\t else \n\t\t\t" + n.s2.accept(this) + ";";
    }

    @Override
    public String visit(While n) {
        return "while( " + n.e.accept(this) + ") " + n.s.accept(this);
    }

    @Override
    public String visit(Print n) {
        return "System.out.println(" + n.e.accept(this) + "); \n";
    }

    @Override
    public String visit(Assign n) {
        return n.i.accept(this) + " =" + n.e.accept(this) + ";";

    }

    @Override
    public String visit(ArrayAssign n) {
        return n.i.accept(this) + n.e1.accept(this) + " = " + n.e2.accept(this) + ";";

    }

    @Override
    public String visit(And n) {
        return n.e1.accept(this) + " && " + n.e2.accept(this);

    }

    @Override
    public String visit(Or n) {
        return n.e1.accept(this) + " || " + n.e2.accept(this);


    }

    @Override
    public String visit(LessThan n) {
        n.e1.accept(this);
        return n.e1.accept(this) + " < " + n.e2.accept(this);

    }

    @Override
    public String visit(Equals n) {
        return n.e1.accept(this) + " == " + n.e2.accept(this);
    }

    @Override
    public String visit(Plus n) {
        return n.e1.accept(this) + " + " + n.e2.accept(this);
    }

    @Override
    public String visit(Minus n) {
        return n.e1.accept(this) + " - " + n.e2.accept(this);
    }

    @Override
    public String visit(Times n) {
        return n.e1.accept(this) + " " + n.e2.accept(this);
    }

    @Override
    public String visit(Divide n) {
        return n.e1.accept(this) + " / " + n.e2.accept(this);
    }

    @Override
    public String visit(ArrayLookup n) {
        return  n.e1.accept(this) + n.e2.accept(this);
    }

    @Override
    public String visit(ArrayLength n) {
        return n.e.accept(this) + ".length";
    }

    @Override
    public String visit(Call n) {
        return n.e.accept(this) + "." + n.i.s+"_#error#_(" + visit(n.el) + ")";
    }

    public String visit(ExpList l){
        StringBuilder sb = new StringBuilder();
        Iterator<Exp> itr = l.list.iterator();
        if(itr.hasNext()) {
            sb.append(itr.next().accept(this));
            while (itr.hasNext()) {
                sb.append(", ");
                sb.append(itr.next().accept(this));
            }
        }
        return sb.toString();
    }

    @Override
    public String visit(IntegerLiteral n) {
        return Integer.toString(n.i);
    }

    @Override
    public String visit(StringLiteral n) {
        return n.val;
    }

    @Override
    public String visit(True n) {
        return "true ";
    }

    @Override
    public String visit(False n) {
        return "false ";
    }

    @Override
    public String visit(IdentifierExp n) {
        return n.i.accept(this);
    }

    @Override
    public String visit(This n) {
        return "this";
    }

    @Override
    public String visit(NewArray n) {
        return "new " + n.e.accept(this);
    }

    @Override
    public String visit(NewObject n) {
        return "new " + n.i.accept(this);
    }

    @Override
    public String visit(Not n) {
        return "!" + n.e.accept(this);

    }

    @Override
    public String visit(Identifier n) {
        if (!id.containsKey(n.b)) { id.put(n.b, n.s + '_' + (idCount++) + "_"); }
        return id.get(n.b);
    }

    @Override
    public String visit(Sidef n) {
        return n.e.accept(this);
    }
}
