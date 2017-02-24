import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer2 {

    char[] cbuf;
    int col, row, i;

    Lexer2(String fileName) throws IOException {
        cbuf = new char[(int)new File(fileName).length() + 1];
        Files.newBufferedReader(Paths.get(fileName)).read(cbuf);
        cbuf[cbuf.length-1] = (char)0;
    }

    public static void main(String[] args) throws IOException {
        Lexer2 l = new Lexer2("src/main/java/a.emj");
        System.out.println(l.cbuf);
        Token t;
        while((t = l.next()).t != TokenType.EOF) {
            if(t instanceof IdentifierToken) {
                System.out.print(((IdentifierToken) t).value);
            }
            System.out.println(t.t);
        }
    }

    Token next() {
        switch(cbuf[i++]) {
            // ---- EOF ----
            case(char)0: return new Token(row, col, TokenType.EOF);
            //---- whitespace ----
            case'\n':   col = 1;  row++; return next();
            case' ':    col++;           return next();
            case'\t':   col +=4;         return next();
            //---- identifiers and keywords -----
            case'a':case'b':case'c':case'd':case'e':case'f':case'g':case'h':case'i':case'j':case'k':case'l':case'm':
            case'n':case'o':case'p':case'q':case'r':case's':case't':case'u':case'v':case'w':case'x':case'y':case'z':
                StringBuilder b = new StringBuilder().append(cbuf[i - 1]);
                while(i < cbuf.length && Character.isLetterOrDigit(cbuf[i])) b.append(cbuf[i++]);
                switch (b.toString()) {
                    case"class":    return new Token(row, col, TokenType.CLASS);
                    case"public":   return new Token(row, col, TokenType.PUBLIC);
                    case"static":   return new Token(row, col, TokenType.STATIC);
                    case"extends":  return new Token(row, col, TokenType.EXTENDS);
                    case"int":      return new Token(row, col, TokenType.INT);
                    case"boolean":  return new Token(row, col, TokenType.BOOLEAN);
                    case"while":    return new Token(row, col, TokenType.WHILE);
                    case"if":       return new Token(row, col, TokenType.IF);
                    case"else":     return new Token(row, col, TokenType.ELSE);
                    case"main":     return new Token(row, col, TokenType.MAIN);
                    case"return":   return new Token(row, col, TokenType.RETURN);
                    case"length":   return new Token(row, col, TokenType.LENGTH);
                    case"true":     return new Token(row, col, TokenType.TRUE);
                    case"false":    return new Token(row, col, TokenType.FALSE);
                    case"this":     return new Token(row, col, TokenType.THIS);
                    case"new":      return new Token(row, col, TokenType.NEW);
                    case"sidef":    return new Token(row, col, TokenType.SIDEF);
                    case"System":
                        if(i+11 < cbuf.length && cbuf[i] == '.'   && cbuf[++i] == 'o' && cbuf[++i] == 'u' && cbuf[++i] == 't' &&
                             cbuf[++i] == '.' && cbuf[++i] == 'p' && cbuf[++i] == 'r' && cbuf[++i] == 'i' && cbuf[++i] == 'n' &&
                             cbuf[++i] == 't' && cbuf[++i] == 'l' && cbuf[++i] == 'n') {
                                    return new Token(row, col, TokenType.PRINTLN);
                        } else return null; //TODO: ERROR
                    default:        return new IdentifierToken(row, col, b.toString());
                }
            //---- int literals ----
            case'0':case'1':case'2':case'3':case'4':case'5':case'6':case'7':case'8':case'9':
                int val = 0;
                while(i < cbuf.length && Character.isDigit(cbuf[i])) val = 10*val + Character.getNumericValue(cbuf[i++]);
                return new IntLiteralToken(row, col, val);
            //---- symbol keywords ----
            case':':    return new Token(row, col, TokenType.COLON);
            case';':    return new Token(row, col, TokenType.SEMICOLON);
            case'.':    return new Token(row, col, TokenType.DOT);
            case',':    return new Token(row, col, TokenType.COMMA);
            case'!':    return new Token(row, col, TokenType.BANG);
            case'(':    return new Token(row, col, TokenType.LPAREN);
            case')':    return new Token(row, col, TokenType.RPAREN);
            case'[':    return new Token(row, col, TokenType.LBRACKET);
            case']':    return new Token(row, col, TokenType.RBRACKET);
            case'{':    return new Token(row, col, TokenType.LBRACE);
            case'}':    return new Token(row, col, TokenType.RBRACE);
            case'<':    return new Token(row, col, TokenType.LESSTHAN);
            case'+':    return new Token(row, col, TokenType.PLUS);
            case'-':    return new Token(row, col, TokenType.MINUS);
            case'*':    return new Token(row, col, TokenType.TIMES);
            case'=':
                if(i < cbuf.length && cbuf[i] == '=')
                        return new Token(row, col, TokenType.EQUALS);
                else    return new Token(row, col, TokenType.EQSIGN);
            case'&':
                if(i < cbuf.length && cbuf[i] == '&')
                        return new Token(row, col, TokenType.AND);
                else    return null; //TODO: ERROR
            case'|':
                if(i < cbuf.length && cbuf[i] == '|')
                        return new Token(row, col, TokenType.OR);
                else    return null; //TODO: ERROR
            case'/':
                if(cbuf[i] == (char)0) return new Token(row, col, TokenType.EOF);
                switch(cbuf[i]) {
                    case'/':
                        while(cbuf[i] != (char)0 && cbuf[i] != '\n') i++;
                        return next();
                    case'*':
                        while(++i < cbuf.length && cbuf[i] != '*' && i+1 < cbuf.length && cbuf[i+1] != '/');
                        return next();
                    default:
                        return new Token(row, col, TokenType.DIV);
                }
            default: return null; //TODO: ERROR
        }
    }
}
