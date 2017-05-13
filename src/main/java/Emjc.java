import DeadCodeElimination.DeadCodeEliminator;
import ast.ClassDeclaration.ClassDecl;
import ast.Program;
import graphs.AlgebraicSimplification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Contains the main method for the E mini Java Compiler.
 */
public class Emjc {

    static StringBuilder errors;
    static PrintStream output = System.out;

    public static void main(String[] args) {
        errors = new StringBuilder();
        if (args.length == 2) {
            switch (args[0]) {
                case "--lex":
                    new Lexer(args[1]).genLexFile();
                    return;
                case "--ast":
                    try (BufferedWriter b = Files.newBufferedWriter(Paths.get(args[1].replace(".emj", ".ast")))) {
                        b.write(new Parser(new Lexer(args[1])).program().accept(new ASTPrinter()));
                        b.flush();
                    } catch (Exception e) {
                        System.out.println("\t" + e.getMessage());
                    }
                    return;
                case "--name":
                    String s = new SymbolGenerator().visit(new Parser(new Lexer(args[1])).program());
                    if (s.isEmpty()) output.println("Valid eMiniJava Program");
                    return;
                case "--pp":
                    Program p = new Parser(new Lexer(args[1])).program();
                    new SymbolGenerator().visit(p);
                    new PrettyPrinter().visit(p);
                    return;
                case "--type":
                    Program p1 = new Parser(new Lexer(args[1])).program();
                    errors.append(new SymbolGenerator().visit(p1));
                    new TypeAnalysis().visit(p1);
                    errors.append(TypeAnalysis.output);
                    if (errors.toString().isEmpty()) output.println("Valid eMiniJava Program");
                    else output.println(errors.toString());
                    return;
                case "--cgen":
                    // parse
                    Program p2 = new Parser(new Lexer(args[1])).program();
                    // name analysis
                    errors.append(new SymbolGenerator().visit(p2));
                    // type analysis
                    if (errors.toString().isEmpty()) {
                        new TypeAnalysis().visit(p2);
                        errors.append(TypeAnalysis.output);
                    }

                    if (!errors.toString().isEmpty()) {
                        output.println(errors.toString());
                        return;
                    }



                    // class file generation with jasmin
                    String path = args[1].substring(0, args[1].lastIndexOf("/") + 1);
                    ClassFileGenerator cfg = new ClassFileGenerator();
                    Stream.concat(
                            Stream.of(p2.m).map(c -> new String[]{c.i.s, cfg.visit(c)}),
                            p2.cl.list.stream().map(c -> new String[]{c.i.s, cfg.visit(c)})
                    ).forEach(classInfo -> {
                        try(BufferedWriter b = Files.newBufferedWriter(Paths.get(path + classInfo[0] + ".j"))) {
                            b.write(classInfo[1]); b.flush();
                            String[] cmd = {"java", "-jar", "jasmin.jar", "-d", path, path+classInfo[0]+".j"};
                            Process process = Runtime.getRuntime().exec(cmd);
                            //TODO: remove this
//                            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                            stdOutput.lines().forEach(System.out::println);
//                            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//                            stdError.lines().forEach(System.out::println);
                            //--------------

                            process.waitFor();
                        } catch (Exception e) {
                        }
                    });
                    return;
                case "--opt": //TODO
                    //Emjc.main(new String[]{"--cgen", args[1]});
                    Program p3 = new Parser(new Lexer(args[1])).program();
                    // name analysis
                    errors.append(new SymbolGenerator().visit(p3));
                    // type analysis
                    new TypeAnalysis().visit(p3);
                    errors.append(TypeAnalysis.output);
                    if (!errors.toString().isEmpty()) {
                        output.println(errors.toString());
                        return;
                    }
                    p3 = (Program) new DeadCodeEliminator().visit(p3);
//                    AlgebraicSimplification var = new AlgebraicSimplification();
//                    var.visit(p3);
                    // class file generation with jasmin
                    String path1 = args[1].substring(0, args[1].lastIndexOf("/") + 1);
                    ClassFileGenerator cfg1 = new ClassFileGenerator();
                    Stream.concat(
                            Stream.of(p3.m).map(c -> new String[]{c.i.s, cfg1.visit(c)}),
                            p3.cl.list.stream().map(c -> new String[]{c.i.s, cfg1.visit(c)})
                    ).forEach(classInfo -> {
                        try(BufferedWriter b1 = Files.newBufferedWriter(Paths.get(path1 + classInfo[0] + ".j"))) {
                            b1.write(classInfo[1]); b1.flush();
                            String[] cmd = {"java", "-jar", "jasmin.jar", "-d", path1, path1+classInfo[0]+".j"};
                            Process process = Runtime.getRuntime().exec(cmd);
                            process.waitFor();
                        } catch (Exception e) {}});
                    return;
                case "--optinfo":
                    // generate unoptimized class file
                    Emjc.main(new String[]{"--cgen", args[1]});
                    // get the number of instructions in all the generated class files
                    System.out.println("#bytecode before optimization: " + countByteCodeInstructions(args[1]));

                    // generate optimized class file
                    Emjc.main(new String[]{"--opt", args[1]});
                    //get the number of instructions in all the generated class files
                    System.out.println("#bytecode after optimization: " + countByteCodeInstructions(args[1]));
                    return;
            }
        }

        //print --help info
        System.out.println("Usage: emjc [option] [source file]");
        System.out.println("\nOptions:\n\t--lex\t\tgenerates output from lexical analysis");
        System.out.println("\t--ast\t\tgenerates output from syntactic analysis");
        System.out.println("\t--pp\t\tPretty-prints the input file to the standard output");
        System.out.println("\t--name\t\tGenerates output from name analysis");
        System.out.println("\t--type\t\tGenerates output from type analysis");
        System.out.println("\t--cgen\t\tGenerates output from intermediate code generation");
        System.out.println("\t--opt\t\tGenerates optimized executable program");
        System.out.println("\t--optinfo\tGenerates optimized executable program with some statistics");
        System.out.println("\t--help\t\tPrints a synopsis of options");
    }

    private static long countByteCodeInstructions(String srcFilePath) {
        String path = srcFilePath.substring(0, srcFilePath.lastIndexOf("/") + 1);
        Program p = new Parser(new Lexer(srcFilePath)).program();
        return Stream.concat(Stream.of(p.m), p.cl.list.stream())
                .map(c -> c.i.s)
                .map(className -> path + className)
                .mapToLong(classPath -> {
                    try {
                        InputStream i = Runtime.getRuntime().exec("javap -c " + classPath.replace(".emj", ".class")).getInputStream();
                        long ret = new BufferedReader(new InputStreamReader(i)).lines()
                                .map(String::trim).filter(line -> !line.isEmpty())
                                .filter(line -> Character.isDigit(line.charAt(0)))
                                .count();
                        Files.delete(Paths.get(classPath + ".class"));
                        Files.delete(Paths.get(classPath + ".j"));
                        return ret;
                    } catch (Exception e) {}
                    return 0;
                }).sum();
    }
}
