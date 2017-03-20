/**
 * Contains the main method for the E mini Java Compiler.
 */
public class Emjc {

    public static void main(String[] args) {
        if(args.length == 2) {
            switch(args[0]) {
                case"--lex":
                    new Lexer(args[1]).genLexFile();
                    return;
                case"--ast":
                    //TODO:
                    return;
            }
        }
        //print --help info
        System.out.println("Usage: emjc [option] [source file]");
        System.out.println("\nOptions:\n\t--lex\t\t generates output from lexical analysis");
        System.out.println("\t--ast\t\t generates output from syntactic analysis");
    }
}
