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

public class PrettyPrinter implements Visitor {
    StringBuilder sb = new StringBuilder();
    int idCount = 0;
    HashMap<Decl, String> id = new HashMap<>();

    @Override
    public void visit(Program n) {
        n.m.accept(this);
        n.cl.list.forEach(c -> c.accept(this));
        System.out.println(sb.toString());
    }

    @Override
    public void visit(MainClass n) {
        sb.append("class ");
        n.i.accept(this);
        sb.append(" {\n\tpublic static void main(String [] ");
        n.i2.accept(this);
        sb.append(") {");
        n.s.accept(this);
        sb.append("\t} \n }\n");
    }

    @Override
    public void visit(ClassDeclSimple n) {
        sb.append("class ");
        n.i.accept(this);
        sb.append(" {\n");
        n.vl.list.forEach(v-> {sb.append("\t"); v.accept(this);});
        n.ml.list.forEach(m -> {sb.append("\t"); m.accept(this);});
        sb.append("\t} \n}");
    }

    @Override
    public void visit(ClassDeclExtends n) {
        sb.append("class ");
        n.i.accept(this);
        sb.append(" extends ");
        n.parent.accept(this);
        sb.append(" {\n");
        n.vl.list.forEach(v-> {sb.append("\t"); v.accept(this);});
        n.ml.list.forEach(m -> {sb.append("\t"); m.accept(this);});
        sb.append("\t} \n}");
    }

    @Override
    public void visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    @Override
    public void visit(MethodDecl n) {
        sb.append("public ");
        n.t.accept(this);
        n.i.accept(this);
        visit(n.fl);
        n.vl.list.forEach(v-> {sb.append("\n\t\t"); v.accept(this);});
        n.sl.list.forEach(s->{sb.append("\n\t\t"); s.accept(this);});
        n.e.accept(this);
    }

    public void visit(FormalList l) {
        sb.append("(");
        Iterator<Formal> itr = l.list.iterator();
        if(itr.hasNext()) itr.next().accept(this);
        while(itr.hasNext()){
            sb.append(", ");
            itr.next().accept(this);
        }
        sb.append(") {");

    }
    @Override
    public void visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    @Override
    public void visit(IntArrayType n) {
        sb.append("int[] ");
    }

    @Override
    public void visit(BooleanType n) {
        sb.append("boolean");
    }

    @Override
    public void visit(IntegerType n) {
        sb.append("int ");
    }

    @Override
    public void visit(StringType n) {
        sb.append("String ");
    }

    @Override
    public void visit(IdentifierType n) {
        n.i.accept(this);
    }

    @Override
    public void visit(Block n) {
        sb.append("{");
        n.sl.list.forEach(s->{sb.append("\n\t");s.accept(this);});
        sb.append("\n}");
    }

    @Override
    public void visit(If n) {
        sb.append("if ("); n.e.accept(this); sb.append(") \n");
        sb.append("\t");
        n.s1.accept(this);
        sb.append("\n else ");
        n.s2.accept(this);
    }

    @Override
    public void visit(While n) {
        sb.append("while ("); n.e.accept(this); sb.append(") ");
        n.s.accept(this);
    }

    @Override
    public void visit(Print n) {
        sb.append("System.out.println("); n.e.accept(this);sb.append(");");
    }

    @Override
    public void visit(Assign n) {
        n.i.accept(this);
        sb.append(" = ");
        n.e.accept(this);
    }

    @Override
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        n.e1.accept(this);
        sb.append(" = ");
        n.e2.accept(this);
    }

    @Override
    public void visit(And n) {
        n.e1.accept(this);
        sb.append(" && ");
        n.e2.accept(this);
    }

    @Override
    public void visit(Or n) {
        n.e1.accept(this);
        sb.append(" || ");
        n.e2.accept(this);

    }

    @Override
    public void visit(LessThan n) {
        n.e1.accept(this);
        sb.append(" < ");
        n.e2.accept(this);
    }

    @Override
    public void visit(Equals n) {
        n.e1.accept(this);
        sb.append(" == ");
        n.e2.accept(this);
    }

    @Override
    public void visit(Plus n) {
        n.e1.accept(this);
        sb.append(" + ");
        n.e2.accept(this);
    }

    @Override
    public void visit(Minus n) {
        n.e1.accept(this);
        sb.append(" - ");
        n.e2.accept(this);
    }

    @Override
    public void visit(Times n) {
        n.e1.accept(this);
        sb.append(" *");
        n.e2.accept(this);
    }

    @Override
    public void visit(Divide n) {
        n.e1.accept(this);
        sb.append(" / ");
        n.e2.accept(this);
    }

    @Override
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    @Override
    public void visit(ArrayLength n) {
        n.e.accept(this);
        sb.append(".length ");
    }

    @Override
    public void visit(Call n) {
        n.e.accept(this);
        sb.append(".");
        //Name analysis method names are unresolved symbols
        sb.append(n.i.s+"_#error#_");
        sb.append("(");
        visit(n.el);
        sb.append(")");
    }

    public void visit(ExpList l){
        Iterator<Exp> itr = l.list.iterator();
        if(itr.hasNext()) itr.next().accept(this);
        while(itr.hasNext()){
            sb.append(", ");
            itr.next().accept(this);
        }
    }

    @Override
    public void visit(IntegerLiteral n) {
        sb.append(n.i);
    }

    @Override
    public void visit(StringLiteral n) {
        sb.append(n.val);
    }

    @Override
    public void visit(True n) {
        sb.append("true ");
    }

    @Override
    public void visit(False n) {
        sb.append("false ");
    }

    @Override
    public void visit(IdentifierExp n) {
        n.i.accept(this);
    }

    @Override
    public void visit(This n) {
        sb.append("this ");
    }

    @Override
    public void visit(NewArray n) {
        sb.append("new ");
        n.e.accept(this);
    }

    @Override
    public void visit(NewObject n) {
        sb.append("new ");
        n.i.accept(this);
    }

    @Override
    public void visit(Not n) {
        sb.append("!");
        n.e.accept(this);
    }

    @Override
    public void visit(Identifier n) {
        if (!id.containsKey(n.b)) { id.put(n.b, n.s + '_' + (idCount++) + "_"); }
        sb.append(id.get(n.b));
    }
}
