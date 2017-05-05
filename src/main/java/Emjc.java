import ast.ClassDeclaration.ClassDecl;
import ast.Program;

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
                    new TypeAnalysis().visit(p2);
                    errors.append(TypeAnalysis.output);
                    if (!errors.toString().isEmpty()) { output.println(errors.toString()); return; }


                    // class file generation with jasmin
//                    args[1] = args[1].replace(".emj", ".j");
                    String path = args[1].substring(0, args[1].lastIndexOf("/") + 1);
                    ClassFileGenerator cfg = new ClassFileGenerator();
                    Stream.concat(
                            Stream.of(p2.m).map(c -> new String[] {c.i.s, cfg.visit(c)}),
                            p2.cl.list.stream().map(c -> new String[] {c.i.s, cfg.visit(c)})
                    ).forEach(classInfo -> {
                        // classInfo[0] = class name       classInfo[1] = jasmin input file
                        try(BufferedWriter b = Files.newBufferedWriter(Paths.get(path + classInfo[0] + ".j"))) {
                            b.write(classInfo[1]); b.flush();
                            Process process = Runtime.getRuntime().exec("java -jar jasmin.jar -d " + path + " " + path + classInfo[0] + ".j");

                            //TODO: remove this
                            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            stdOutput.lines().forEach(System.out::println);
                            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                            stdError.lines().forEach(System.out::println);
                            //--------------

                            process.waitFor();
                        } catch (Exception e) {}
                    });
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
        System.out.println("\t--help\t\tPrints a synopsis of options");
    }

    public static void genClassFile(String jasminContents, String fileName) {

    }

}
