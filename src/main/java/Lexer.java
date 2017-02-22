import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lexer {

    private static class Char {
        int row, col; char val;
        Char(int row, int col, char val) { this.row = row; this.col = col; this.val = val; }
    }

    ConcurrentLinkedQueue<Char> chars = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Token> tokens = new ConcurrentLinkedQueue<>();

    AtomicBoolean allCharactersTokenized = new AtomicBoolean(false);
    String fileName;

    Lexer(String fileName) {
        this.fileName = fileName;
        AtomicBoolean allCharactersRead = new AtomicBoolean(false);
        new Thread(() -> {
            try(BufferedReader file = Files.newBufferedReader(Paths.get(fileName))) {
                // -- char stream logic --

            } catch(IOException e) {}
            allCharactersRead.set(true);
        }).start();

        new Thread(() -> {
            Char c = chars.poll();
            int row, col;
            while(c != null || allCharactersRead.get()) {
                row = c.row; col = c.col;
                if(Character.isLetter(c.val)) { // identifier or keyword
                    StringBuffer b = new StringBuffer();
                    do {
                        b.append(c.val);
                        c = chars.poll();
                    } while(c != null && (Character.isLetter(c.val) || Character.isDigit(c.val)));
                    switch(b.toString()) {
                        case "class":
                            tokens.offer(new Token(row, col, Token.TokenType.CLASS));
                            continue;
                        case "public":
                            tokens.offer(new Token(row, col, Token.TokenType.PUBLIC));
                            continue;
                        case "static":
                            tokens.offer(new Token(row, col, Token.TokenType.STATIC));
                            continue;
                        case "void":
                            tokens.offer(new Token(row, col, Token.TokenType.VOID));
                            continue;
                        case "String":
                            tokens.offer(new Token(row, col, Token.TokenType.STRING));
                            continue;
                        case "extends":
                            tokens.offer(new Token(row, col, Token.TokenType.EXTENDS));
                            continue;
                        case "int":
                            tokens.offer(new Token(row, col, Token.TokenType.INT));
                            continue;
                        case "boolean":
                            tokens.offer(new Token(row, col, Token.TokenType.BOOLEAN));
                            continue;
                        case "while":
                            tokens.offer(new Token(row, col, Token.TokenType.WHILE));
                            continue;
                        case "if":
                            tokens.offer(new Token(row, col, Token.TokenType.IF));
                            continue;
                        case "else":
                            tokens.offer(new Token(row, col, Token.TokenType.ELSE));
                        case "main":
                            tokens.offer(new Token(row, col, Token.TokenType.MAIN));
                            continue;
                        case "return":
                            tokens.offer(new Token(row, col, Token.TokenType.RETURN));
                            continue;
                        case "length":
                            tokens.offer(new Token(row, col, Token.TokenType.LENGTH));
                            continue;

                        //TODO more cases
                        case "System":
                            do { //TODO: make more efficient
                                b.append(c);
                                c = chars.poll();
                            } while(c != null && "System.out.println".contains(b.toString()));
                            if(b.toString().equals("System.out.println")) {
                                tokens.offer(new Token(row, col, Token.TokenType.PRINTLN));
                            }
                            break;
                        default: // identifier
                            tokens.offer(new Token(row, col, Token.TokenType.ID, b.toString()));
                    }

                } else if(Character.isDigit(c.val)) {
                    //TODO
                } else switch(c.val) {
                    //TODO
                    default:
                }
                //TODO
            }

            allCharactersTokenized.set(true);
        }).start();
    }

    void genLexFiles() throws IOException {
        while(!allCharactersTokenized.get());
        List<String> tokenStrings = tokens.stream().map(Token::toString).collect(Collectors.toList());
        Files.write(Paths.get(fileName), tokenStrings);
    }
}
