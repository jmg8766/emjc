import ast.*;
import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassFileGenerator implements Visitor<String> {

    private HashMap<Decl, Integer> localVars = new HashMap<>();
    private int localVarsIndex = 0;
    private int i;

    @Override
    public String visit(Program n) {
        StringBuilder b = new StringBuilder(n.m.accept(this));
        n.cl.list.forEach(c -> b.append(c.accept(this)).append("\n"));
        return b.toString();
    }

    @Override
    public String visit(MainClass n) {
        return ".class " + n.i.s + "\n" +
                ".super java/lang/Object\n\n" +
                ".method public <init>()V\n" +
                "\taload_0\n" +
                "\tinvokespecial java/lang/Object/<init>()V\n" +
                "\treturn\n" +
                ".end method\n\n" +
                ".method public static main([Ljava/lang/String;)V\n" +
                ".limit stack 9\n" + //TODO: limit stack and locals
                ".limit locals 1\n" +
                n.s.accept(this) + "\n" +
                "return\n" +
                ".end method";
    }

    public String visit(ClassDecl n) {
        return n.accept(this);
    }

    @Override
    public String visit(ClassDeclSimple n) {
        return ".class " + n.i.s + "\n" +
                ".super java/lang/Object\n\n" +
                n.vl.list.stream().map(v -> v.accept(this)).collect(Collectors.joining("\n")) + "\n" +
                ".method public <init>()V\n" +
                "\taload_0\n" +
                "\tinvokespecial java/lang/Object/<init>()V\n" +
                "\treturn\n" +
                ".end method\n\n" +
                n.ml.list.stream().map(m -> m.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(ClassDeclExtends n) {
        return ".class " + n.i.s + "\n" +
                ".super " + n.parent.s + "\n\n" +
                n.vl.list.stream().map(v -> v.accept(this)).collect(Collectors.joining("\n")) +
                ".method public <init>()V\n" +
                "\taload_0\n" +
                "\tinvokespecial java/lang/Object/<init>()V\n" +
                "\treturn\n" +
                ".end method\n\n" +
                n.ml.list.stream().map(m -> m.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(VarDecl n) {
        // only handles class instance variables
        return ".field public " + n.i.s + " " + n.t.accept(this);
    }

    public void localVarDecl(Decl n) {
        localVars.put(n, localVarsIndex++);
    }

    @Override
    public String visit(MethodDecl n) {
        String parameters = n.fl.list.stream().map(f -> f.accept(this)).collect(Collectors.joining(";"));
        n.vl.list.forEach(this::localVarDecl);
        String type = (n.t instanceof StringType ? "a" : n.t.accept(this).toLowerCase());
        if(type.equals("z")) type = "i";
        String ret = ".method public " + n.i.s + "(" + parameters + ")" + n.t.accept(this) + "\n" +
                ".limit stack 9\n" + //TODO set appropriate stack size
                ".limit locals 9\n" + //TODO set appropriate stack size
                n.sl.list.stream().map(s -> s.accept(this)).collect(Collectors.joining()) + "\n" +
                n.e.accept(this) + "\n" +
                type + "return\n" +
                ".end method";
        Stream.concat(n.vl.list.stream(), n.fl.list.stream()).forEach(v -> {
            localVars.remove(v);
            localVarsIndex--;
        });
        return ret;
    }

    @Override
    public String visit(Formal n) {
        localVars.put(n, localVarsIndex++);
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
        return "Ljava/lang/String;";
    }

    @Override
    public String visit(IdentifierType n) {
        return "L" + n.i.s + ";";
    }

    @Override
    public String visit(Block n) {
        return n.sl.list.stream().map(s -> "\t" + s.accept(this)).collect(Collectors.joining("\n"));
    }

    @Override
    public String visit(If n) {
        int labelNum = ++i;
        return ";IF ------------------------\n" +
                n.e.accept(this) + "\n" +
                "ifeq else" + labelNum + "\n" +
                n.s1.accept(this) + "\n" +
                "goto done" + labelNum + "\n" +
                "else" + labelNum + ":\n" +
                n.s2.accept(this) + "\n" +
                "done" + labelNum + ":\n" +
                ";END IF -------------------\n";
    }

    @Override
    public String visit(While n) {
        int labelNum = ++i;
        return ";WHILE ------------------------\n" +
                "while" + labelNum + ":\n" +
                n.e.accept(this) + "\n" +
                "ifeq done" + labelNum + "\n" +
                n.s.accept(this) + "\n" +
                "goto while" + labelNum + "\n" +
                ";END WHILE -------------------\n";
    }

    @Override
    public String visit(Print n) {
        return "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                n.e.accept(this) + "\n" +
                "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V";
    }

    @Override
    public String visit(Assign n) {
        String type = n.i.b.t.accept(this).toLowerCase();
        if(type.equals("z")) type = "i";
        return ";ASSIGN ------------------\n" +
                n.e.accept(this) + "\n" +
                type + "store " + localVars.get(n.i.b) + "\n" +
                ";END-ASSIGN -------------\n";
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
        return n.e.accept(this) + "\n" + // places object on the top of the stack
                n.el.list.stream().map(e -> e.accept(this)).collect(Collectors.joining("\n")) +
                "invokevirtual " + ((IdentifierType)n.e.t).i.s + "/" + n.i.s +
                "(" + n.el.list.stream().map(e -> e.t.accept(this)).collect(Collectors.joining(";")) + ")" +
                n.t.accept(this) + "\n";
    }

    @Override
    public String visit(IntegerLiteral n) {
        return "iload " + n.i;
    }

    @Override
    public String visit(StringLiteral n) {
        return "ldc \"" + n.val + "\"";
    }

    @Override
    public String visit(True n) {
        return "ldc 1";
    }

    @Override
    public String visit(False n) {
        return "ldc 0";
    }

    @Override
    public String visit(IdentifierExp n) {
        return ";IdentifierExp --------------\n" +
                "iload " + localVars.get(n.i.b) + "\n";
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
        return "new " + n.i.s + "\n" +
                "dup\n" +
                "invokespecial " + n.i.s + "/<init>()V";
    }

    @Override
    public String visit(Not n) {
        return null;
    }

    @Override
    public String visit(Identifier n) {
        return ";Identifier----------------------\n"

                ; //TODO
    }
}
