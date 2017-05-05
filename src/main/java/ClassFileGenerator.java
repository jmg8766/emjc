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
    private HashMap<Decl, String> reference = new HashMap<>();
    private int localVarsIndex = 0, i;

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
        n.vl.list.stream().forEach(v -> reference.put(v, n.i.s + "/"+v.i.s));
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
        n.vl.list.stream().forEach(v -> reference.put(v, n.i.s + "/"+v.i.s));
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
        localVars.put(n, ++localVarsIndex);
    }

    @Override
    public String visit(MethodDecl n) {
        String parameters = n.fl.list.stream().map(f -> f.accept(this)).collect(Collectors.joining(";"));
        n.vl.list.forEach(this::localVarDecl);
        String type = (n.t instanceof StringType ? "a" : n.t.accept(this).toLowerCase());
        if (type.equals("z")) type = "i";
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
                (n.s2 != null ? "goto done" + labelNum + "\n" : "") +
                "else" + labelNum + ":\n" +
                (n.s2 != null ? n.s2.accept(this) + "\n" +
                "done" + labelNum + ":\n" : "") +

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
        //TODO Should boolean be true / false instead of 1/0
        String print;
        if(n.e.t instanceof IntegerType || n.e.t instanceof BooleanType)
            print = "invokevirtual java/io/PrintStream/println(I)V\n";
        else
            print = "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V\n";
        return "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                n.e.accept(this) + "\n" +
                print;
    }

    @Override
    public String visit(Assign n) {
        String type = n.i.b.t.accept(this);
        if (type.equals("Z")) type = "I";
        else if(type.equals("Ljava/lang/String;") && localVars.get(n.i.b) != null) type = "A";
        // TODO Handle reference variable
        String var = localVars.get(n.i.b) == null ?
                "aload_0\n"+ n.e.accept(this) + "\n" +"putfield " + reference.get(n.i.b) + " " + type + "\n"
                :
                n.e.accept(this) + "\n" + type.toLowerCase() + "store " + localVars.get(n.i.b) + "\n";
        return ";ASSIGN ------------------\n" +
                var +
                ";END-ASSIGN -------------\n";
    }

    @Override
    public String visit(ArrayAssign n) {
        //TODO
        return null;
    }

    @Override
    public String visit(And n) {
        return ";---- AND ------------------\n" +

                new If(null, n.e1,
                    new If(null, n.e2, // if first expression is true
                        new True(null), // and second expression is true, return true
                        new False(null)), // and second expression if false, return false
                    new False(null)) // if first expression if false, return false
                .accept(this) + "\n" +

                ";----- END-AND ------------";
    }

    @Override
    public String visit(Or n) {
        return ";---- OR ------------------\n" +

                new If(null, n.e1,
                    new True(null), // if first exp is true, return true
                    new If(null, n.e2,
                        new True(null), // if first exp is false, but second is true, return true
                        new False(null)) // if first and second exp are false, return false
                ).accept(this) + "\n" +

               ";----- END-AND ------------";
    }

    @Override
    public String visit(LessThan n) {
        int labelNum = ++i;
        return ";---LESSTHAN --------------\n" +
                n.e1.accept(this) + "\n" +
                n.e2.accept(this) + "\n" +
                "isub\n" +
                "ifle retTrue" + labelNum + "\n" +
                "\ticonst_0\n" +
                "\tgoto done" + labelNum + "\n" +
                "retTrue" + labelNum + ":\n" +
                "\ticonst_1\n" +
                "done" + labelNum + ":\n" +
                ";-------END-LESSTHAN------------\n";
    }

    @Override
    public String visit(Equals n) {
        // ifeq nfalse nfalse: iconst_0 goto end nTrue: iconst_1 end

        int labelNum = ++i;
        StringBuilder sb = new StringBuilder();

        sb.append(";Equals ------------------\n" + n.e1.accept(this) + "\n" + n.e2.accept(this) + "\n");
        if (n.e1.t == n.e2.t && (n.e1.t instanceof IntegerType || n.e1.t instanceof BooleanType)) sb.append("if_icmpeq nTrue" + labelNum + "\n");
        else if (n.e1.t == n.e2.t && n.e1.t instanceof IdentifierType) sb.append("if_acmpeq nTrue" + labelNum + "\n");

        sb.append("nFalse" + labelNum + ":\niconst_0\n" +
                "goto done" + labelNum +"\n");
        sb.append("nTrue" + labelNum + ":\niconst_1\n" +
                "done" + labelNum + ":");

        return sb.toString();
    }

    @Override
    public String visit(Plus n) {
        // String -- new StringBuilder/append/toString()
        //TODO Overriding
        if (n.e1.t == n.e2.t && n.e1.t instanceof IntegerType)
            return n.e1.accept(this) + "\n" + n.e2.accept(this) + "\niadd\n";
        else if ((n.e1.t == IntegerType.getInstance() && n.e2.t == StringType.getInstance()))
            return "new java/lang/StringBuilder\ndup\ninvokespecial java/lang/StringBuilder/<init>()V\n" + n.e1.accept(this) + "\ninvokevirtual java/lang/StringBuilder/append(I)Ljava/lang/StringBuilder;\n" + n.e2.accept(this) +"\ninvokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;\ninvokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;";
        else if (n.e1.t == StringType.getInstance() && n.e2.t == IntegerType.getInstance())
            return "new java/lang/StringBuilder\ndup\ninvokespecial java/lang/StringBuilder/<init>()V\n" + n.e1.accept(this) + "\ninvokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;\n"+ n.e2.accept(this) + "\ninvokevirtual java/lang/StringBuilder/append(I)Ljava/lang/StringBuilder;\ninvokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;";
        else
            return "new java/lang/StringBuilder\ndup\ninvokespecial java/lang/StringBuilder/<init>()V\n" + n.e1.accept(this) + "\ninvokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;\n"+ n.e2.accept(this) + "\ninvokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;\ninvokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;";

    }

    @Override
    public String visit(Minus n) {
            return n.e1.accept(this) + "\n" + n.e2.accept(this) + "\nisub\n";
    }

    @Override
    public String visit(Times n) {
        return n.e1.accept(this) + "\n" + n.e2.accept(this) + "\nimul\n";
    }

    @Override
    public String visit(Divide n) {
        return n.e1.accept(this) + "\n" + n.e2.accept(this) + "\nidiv\n";
    }

    @Override
    public String visit(ArrayLookup n) {
        //TODO
        return null;
    }

    @Override
    public String visit(ArrayLength n) {
        //TODO
        return null;
    }

    @Override
    public String visit(Call n) {
        return n.e.accept(this) + "\n" + // places object on the top of the stack
                n.el.list.stream().map(e -> e.accept(this)).collect(Collectors.joining("\n")) +
                "invokevirtual " + ((IdentifierType) n.e.t).i.s + "/" + n.i.s +
                "(" + n.el.list.stream().map(e -> e.t.accept(this)).collect(Collectors.joining(";")) + ")" +
                n.t.accept(this) + "\n";
    }

    @Override
    public String visit(IntegerLiteral n) {
        return "ldc " + n.i;
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
        //TODO Check if this covers all cases
        String ret = ";-----IDENTIFIER_EXP--------------\n";
        String type = n.i.b.t.accept(this); //TODO Make Common
        String command;
        if(type.equals("Z") || type.equals("I")) command = "iload ";
        else command = "aload ";
        ret += localVars.containsKey(n.i.b) ? command + localVars.get(n.i.b) + "\n" : "aload 0\ngetfield " + reference.get(n.i.b) + " " + type + "\n";
        return ret + ";----END-IDENTIFIER_EXP ------------------\n";
    }

    @Override
    public String visit(This n) {
        return "aload_0";
    }

    @Override
    public String visit(NewArray n) {
        //TODO
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
        return ";---- NOT ---------\n" +
                new If(null, n.e, new False(null), new True(null)).accept(this) + "\n" +
                ";---- END NOT -------\n";
    }

    @Override
    public String visit(Identifier n) {
        return ";Identifier----------------------\n" +
                "; UNIMPLEMENTED"; //TODO
    }
}
