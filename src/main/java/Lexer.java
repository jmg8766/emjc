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
                 //-- char stream logic --

            } catch(IOException e) {}
            allCharactersRead.set(true);
        }).start();

        new Thread(() -> {

        }).start();
    }

    /**
     * @param input the String to tokenize
     * @return a Stream of tokens
     */
    private Stream<Token> tokenize(String input) {
        Stream.Builder<Token> tokens = Stream.builder();
        for(int col = 0, row = 0, i = 0; i < input.length(); col = 0) {
            if(Character.isLetter(input.charAt(i))) {
                StringBuilder b = new StringBuilder();
                do { b.append(input.charAt(i++)); col++; }
                while(i < input.length() && Character.isLetterOrDigit(input.charAt(i)));
                switch (b.toString()) {
                    case "class":   tokens.accept(new Token(row, col, Token.TokenType.CLASS));   break;
                    case "public":  tokens.accept(new Token(row, col, Token.TokenType.PUBLIC));  break;
                    case "static":  tokens.accept(new Token(row, col, Token.TokenType.STATIC));  break;
                    case "void":    tokens.accept(new Token(row, col, Token.TokenType.VOID));    break;
                    case "String":  tokens.accept(new Token(row, col, Token.TokenType.STRING));  break;
                    case "extends": tokens.accept(new Token(row, col, Token.TokenType.EXTENDS)); break;
                    case "int":     tokens.accept(new Token(row, col, Token.TokenType.INT));     break;
                    case "boolean": tokens.accept(new Token(row, col, Token.TokenType.BOOLEAN)); break;
                    case "while":   tokens.accept(new Token(row, col, Token.TokenType.WHILE));   break;
                    case "if":      tokens.accept(new Token(row, col, Token.TokenType.IF));      break;
                    case "else":    tokens.accept(new Token(row, col, Token.TokenType.ELSE));    break;
                    case "main":    tokens.accept(new Token(row, col, Token.TokenType.MAIN));    break;
                    case "return":  tokens.accept(new Token(row, col, Token.TokenType.RETURN));  break;
                    case "length":  tokens.accept(new Token(row, col, Token.TokenType.LENGTH));  break;
                    case "true":    tokens.accept(new Token(row, col, Token.TokenType.TRUE));    break;
                    case "false":   tokens.accept(new Token(row, col, Token.TokenType.FALSE));   break;
                    case "this":    tokens.accept(new Token(row, col, Token.TokenType.THIS));    break;
                    case "new":     tokens.accept(new Token(row, col, Token.TokenType.NEW));     break;
                    case "sidef":   tokens.accept(new Token(row, col, Token.TokenType.SIDEF));   break;
                    case "System":
                        if(!input.substring(i).startsWith("System.out.println")) error(row, col, input);
                        tokens.accept(new Token(row, col, Token.TokenType.PRINTLN));
                        i += 18;
                        break;
                    default: tokens.accept(new Token(row, col, Token.TokenType.ID, b.toString()));
                }
            } else if(Character.isDigit(input.charAt(i))) {
                int val = 0;
                do { val = 10*val + Character.getNumericValue(input.charAt(i++)); col++; }
                while(i < input.length() && Character.isDigit(input.charAt(i)));
            } else switch(input.charAt(i)) {
                case '\n': row++;
                case ' ': case '\t': i++;                                                   break;
                case ':':   tokens.accept(new Token(row, col, Token.TokenType.COLON));      break;
                case ';':   tokens.accept(new Token(row, col, Token.TokenType.SEMICOLON));  break;
                case '.':   tokens.accept(new Token(row, col, Token.TokenType.DOT));        break;
                case '!':   tokens.accept(new Token(row, col, Token.TokenType.BANG));       break;
                case '(':   tokens.accept(new Token(row, col, Token.TokenType.LPAREN));     break;
                case ')':   tokens.accept(new Token(row, col, Token.TokenType.RPAREN));     break;
                case '[':   tokens.accept(new Token(row, col, Token.TokenType.LBRACKET));   break;
                case ']':   tokens.accept(new Token(row, col, Token.TokenType.RBRACKET));   break;
                case '{':   tokens.accept(new Token(row, col, Token.TokenType.LBRACE));     break;
                case '}':   tokens.accept(new Token(row, col, Token.TokenType.RBRACE));     break;
                case '<':   tokens.accept(new Token(row, col, Token.TokenType.LESSTHAN));   break;
                case '+':   tokens.accept(new Token(row, col, Token.TokenType.PLUS));       break;
                case '-':   tokens.accept(new Token(row, col, Token.TokenType.MINUS));      break;
                case '*':   tokens.accept(new Token(row, col, Token.TokenType.TIMES));      break;
                case '/':
                    if(++i < input.length() && input.charAt(i) == '/') {
                        while(++i < input.length() && input.charAt(i) != '\n');
                    } else if(i < input.length() && input.charAt(i) == '*') {
                        while(++i < input.length() && input.charAt(i) != '*' && i+1 < input.length() && input.charAt(i+1) != '/');
                    } else tokens.accept(new Token(row, col, Token.TokenType.DIV));
                    break;
                case '=':
                    if(++i < input.length() && input.charAt(i) == '=') tokens.accept(new Token(row, col, Token.TokenType.EQUALS));
                    else tokens.accept(new Token(row, col, Token.TokenType.EQSIGN));
                    break;
                case '&':
                    if(++i < input.length() && input.charAt(i) == '&') tokens.accept(new Token(row, col, Token.TokenType.AND));
                    else error(row, col, input);
                case '|':
                    if(++i < input.length() && input.charAt(i) == '|') tokens.accept(new Token(row, col, Token.TokenType.OR));
                    else error(row, col, input);
                default: error(row, col, input);
            }
        }
        return tokens.build();
    }

    private void error(int row, int col, String word) {
        System.out.println("lexing error in '" + word + "' on row:" + row + " column:" + col);
        System.exit(1);
    }

    void genLexFile() throws IOException {
        while(!allCharactersTokenized.get());
        List<String> tokenStrings = tokens.stream().map(Token::toString).collect(Collectors.toList());
        Files.write(Paths.get(fileName), tokenStrings);
    }
}
