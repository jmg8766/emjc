import java.util.Arrays;

/**
 * Contains the main method for the E mini Java Compiler.
 */
public class Emjc {

    public static void main(String[] args) {
        if(args.length >= 2 && args[0].equals("--lex")) {
            long time = -System.currentTimeMillis();
            // lex all source files in parallel
            Arrays.stream(args).skip(1).parallel().forEach(file -> new Lexer(file).genLexFile());
            System.out.println((args.length - 1) + " files parsed in " + (time + System.currentTimeMillis()) + " ms");
        } else {
            System.out.println("Usage: java Emjc --lex [source file]... ");
        }
    }
}
