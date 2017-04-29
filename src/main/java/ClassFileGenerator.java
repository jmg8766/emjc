import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

import java.util.stream.Collectors;

public class ClassFileGenerator implements Visitor<String> {

    @Override
    public String visit(Program n) {
        StringBuilder b = new StringBuilder(n.m.accept(this));
        n.cl.list.forEach(c -> b.append(c.accept(this)).append("\n"));
        return b.toString();
    }

    @Override
    public String visit(MainClass n) {
        return ".class public " + n.i.s + "\n" + //TODO: public?
                ".super java/lang/Object\n\n" +
                ".method public <init>()V\n" +
                    "\taload_0\n" +
                    "\tinvokespecial java/lang/Oject/<init>()V\n" +
                    "\treturn\n" +
                ".end method\n\n" +
                //TODO: default constructor?
                ".method public static main([Ljava/lang/String;)V\n" +
                ".limit stack 9\n" + //TODO: limit stack and locals
                ".limit locals 9\n" +
                n.s.accept(this) + "\n" +
                ".end method";
    }

    @Override
    public String visit(ClassDeclSimple n) {
        return ".class public" + n.i.s + "\n" + //TODO: public?
                ".super java/lang/Object\n\n" +
                //TODO: default constructor?
                n.vl.list.stream().map(v -> v.accept(this)).collect(Collectors.joining("\n")) +
                n.ml.list.stream().map(m -> m.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(ClassDeclExtends n) {
        return ".class public" + n.i.s + "\n" + //TODO: public?
                ".super " + n.parent.s + "\n\n" +
                //TODO: default constructor?
                n.vl.list.stream().map(v -> v.accept(this)).collect(Collectors.joining("\n")) +
                n.ml.list.stream().map(m -> m.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(VarDecl n) {
        // only handles class instance variables
        return ".field public " + n.i.s + " " + n.t.accept(this);
    }

    @Override
    public String visit(MethodDecl n) {
        String parameters = n.fl.list.stream().map(f -> f.accept(this)).collect(Collectors.joining(";"));
        return ".method public " + n.i.s + "(" + parameters + ")" + n.t.accept(this) + "\n" +
                n.vl.list.stream().map(v -> v.accept(this)).collect(Collectors.joining("\n")) +
                n.sl.list.stream().map(s -> s.accept(this)).collect(Collectors.joining()) +
                n.e.accept(this) + "\n" +
                n.t.accept(this).toLowerCase() + "return\n" +
                ".end method";
    }

    @Override
    public String visit(Formal n) {
        return n.t.accept(this);
    }

    @Override
    public String visit(IntArrayType n) {
        return "[I";
    }

    @Override
    public String visit(BooleanType n) {
        return "Z";
    }

    @Override
    public String visit(IntegerType n) {
        return "I";
    }

    @Override
    public String visit(StringType n) {
        return "Ljava/lang/String";
    }

    @Override
    public String visit(IdentifierType n) {
        return "V";
    }

    @Override
    public String visit(Block n) {
        return n.sl.list.stream().map(s -> s.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(If n) {
        return null;
    }

    @Override
    public String visit(While n) {
        return null;
    }

    @Override
    public String visit(Print n) {
        return "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                n.e.accept(this) + "\n" +
                "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V";
    }

    @Override
    public String visit(Assign n) {
        return null;
    }

    @Override
    public String visit(ArrayAssign n) {
        return null;
    }

    @Override
    public String visit(And n) {
        return null;
    }

    @Override
    public String visit(Or n) {
        return null;
    }

    @Override
    public String visit(LessThan n) {
        return null;
    }

    @Override
    public String visit(Equals n) {
        return null;
    }

    @Override
    public String visit(Plus n) {
        return null;
    }

    @Override
    public String visit(Minus n) {
        return null;
    }

    @Override
    public String visit(Times n) {
        return null;
    }

    @Override
    public String visit(Divide n) {
        return null;
    }

    @Override
    public String visit(ArrayLookup n) {
        return null;
    }

    @Override
    public String visit(ArrayLength n) {
        return null;
    }

    @Override
    public String visit(Call n) {
        return null;
    }

    @Override
    public String visit(IntegerLiteral n) {
        return null;
    }

    @Override
    public String visit(StringLiteral n) {
        return "ldc \"" + n.val + "\"";
    }

    @Override
    public String visit(True n) {
        return null;
    }

    @Override
    public String visit(False n) {
        return null;
    }

    @Override
    public String visit(IdentifierExp n) {
        return null;
    }

    @Override
    public String visit(This n) {
        return null;
    }

    @Override
    public String visit(NewArray n) {
        return null;
    }

    @Override
    public String visit(NewObject n) {
        return null;
    }

    @Override
    public String visit(Not n) {
        return null;
    }

    @Override
    public String visit(Identifier n) {
        return null;
    }
}
