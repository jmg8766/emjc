import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if(args[0].equals("--lex")) {
            for(int i=1; i< args.length; i++) {
                new Lexer2(args[i]).genLexFiles();
            }
        }
    }
}
