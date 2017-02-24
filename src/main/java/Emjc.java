import java.io.IOException;

public class Emjc {

    public static void main(String[] args) throws IOException {
        if(args.length == 2 && args[0] == "--lex") {
            new Lexer(args[1]).genLexFile();
        } else {
            System.out.println("Usage java Emjc --lex <source file>");
        }
    }
}
