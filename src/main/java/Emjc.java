import ast.Program;

import java.io.BufferedWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
					if(s.isEmpty()) output.println("Valid eMiniJava Program");
					return;
				case "--type":
					Program p1 = new Parser(new Lexer(args[1])).program();
					errors.append(new SymbolGenerator().visit(p1));
					new TypeAnalysis().visit(p1);
					errors.append(TypeAnalysis.output);
					if(errors.toString().isEmpty()) output.println("Valid eMiniJava Program");
					else output.println(errors.toString());
					return;
				case "--pp":
					Program p = new Parser(new Lexer(args[1])).program();
					new SymbolGenerator().visit(p);
					new PrettyPrinter().visit(p);
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
		System.out.println("\t--help\t\tPrints a synopsis of options");
	}
}
