import token.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer {
    // the names of the file being lexed and the tokenized file
    public String inputFile, outputFile;
    // an array of all characters in the inputFile
    private char[] cbuf;
    // the current row; the index in cbuf; the start of the current row; the number of tabs * 4
    private int row = 1, i, rowStart, tabSpaces;

    /**
     * Constructs a Lexer by reading an entire file into a char array.
     *
     * @param fileName - the name of the input file
     */
    public Lexer(final String fileName) {
        try (BufferedReader file = Files.newBufferedReader(Paths.get(fileName))) {
            // read the entire file into a char array
            cbuf = new char[(int)new File(fileName).length() + 1];
            file.read(cbuf);
            file.close();
            // insert a 0 to represent the EOF
            cbuf[cbuf.length - 1] = (char)0;
            inputFile = fileName;
            outputFile = fileName.substring(0, fileName.indexOf('.')) + ".lexed";
        } catch(Exception e) {
            System.out.println("An IO error occurred while attempting to read " + fileName);
        }
    }

    /**
     *  Lexes a file by repeatedly calling the next() method until EOF is reached.
     */
    void genLexFile() {
        try (BufferedWriter out = Files.newBufferedWriter(Paths.get(outputFile))) {
            Token token = next();
            do {
                out.write(token.toString() + '\n');
            } while((token = next()).type != TokenType.EOF);

            out.write(token.toString() + '\n');
            out.flush(); out.close();
        } catch(IOException e ) {
            System.out.println("An IO error occurred while attempting to write to " + outputFile);
        }
    }

    /**
     * @return the next token found in the input file, the EOF token if the end
     * of file has been reached, or a BAD token if the next characters do not
     * form a valid token.
     */
    Token next() {
        switch(cbuf[i++]) {
            // ---- EOF ----
            case(char)0: return new Token(row, (i - rowStart) + tabSpaces, TokenType.EOF);
            //---- whitespace ----
            case'\r': if(cbuf[i] == '\n') i++; // mac newline = '\r' windows newline = "\r\n"
            case'\n': row++; rowStart = i + tabSpaces;    return next();
            case'\t': tabSpaces+=3;                       return next();
            case' ':                                      return next();
            //---- identifiers and keywords -----
            case'a':case'b':case'c':case'd':case'e':case'f':case'g':case'h':case'i':case'j':case'k':case'l':case'm':
            case'n':case'o':case'p':case'q':case'r':case's':case't':case'u':case'v':case'w':case'x':case'y':case'z':
            case'A':case'B':case'C':case'D':case'E':case'F':case'G':case'H':case'I':case'J':case'K':case'L':case'M':
            case'N':case'O':case'P':case'Q':case'R':case'S':case'T':case'U':case'V':case'W':case'X':case'Y':case'Z':
            case'_':
                StringBuilder id = new StringBuilder().append(cbuf[i - 1]);
                int col = (i-rowStart) + tabSpaces;
                while(i < cbuf.length && (Character.isLetterOrDigit(cbuf[i])) || cbuf[i] == '_') id.append(cbuf[i++]);
                switch (id.toString()) {
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
                    case"void":     return new Token(row, col, TokenType.VOID);
                    case"String":   return new Token(row, col, TokenType.STRING);
                    case"System":
                        if(i + 11 < cbuf.length && cbuf[i++] == '.' && cbuf[i++] == 'o' && cbuf[i++] == 'u' &&
                               cbuf[i++] == 't' && cbuf[i++] == '.' && cbuf[i++] == 'p' && cbuf[i++] == 'r' &&
                               cbuf[i++] == 'i' && cbuf[i++] == 'n' && cbuf[i++] == 't' && cbuf[i++] == 'l' &&
                               cbuf[i++] == 'n') {
                                    return new Token(row, col, TokenType.PRINTLN);
                        } else throw new RuntimeException("Error while lexing " + inputFile + " at row: " + row + " column: " + (i-rowStart) + tabSpaces);
                    default:        return new IdentifierToken(row, col, id.toString());
                }
            //---- int literals ----
            case'0':case'1':case'2':case'3':case'4':case'5':case'6':case'7':case'8':case'9':
                col = (i - rowStart) + tabSpaces;
                int val = Character.getNumericValue(cbuf[i - 1]);
                while(cbuf[i] != (char)0 && Character.isDigit(cbuf[i])) {
                    val = (10 * val) + Character.getNumericValue(cbuf[i++]);
                }
                return new IntLiteralToken(row, col, val);
            //---- symbol keywords ----
            case':':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.COLON);
            case';':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.SEMICOLON);
            case'.':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.DOT);
            case',':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.COMMA);
            case'!':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.BANG);
            case'(':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.LPAREN);
            case')':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.RPAREN);
            case'[':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.LBRACKET);
            case']':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.RBRACKET);
            case'{':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.LBRACE);
            case'}':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.RBRACE);
            case'<':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.LESSTHAN);
            case'+':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.PLUS);
            case'-':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.MINUS);
            case'*':    return new Token(row, (i - rowStart) + tabSpaces, TokenType.TIMES);
            case'"':
                StringBuilder str = new StringBuilder();
                while(true) {
                    switch (cbuf[i]) {
                        case(char)0:case'\n': throw new RuntimeException("Error while lexing " + inputFile + " at row: " + row + " column: " + (i-rowStart) + tabSpaces);
                        case'"': i++;         return new StringLiteralToken(row, (i - rowStart) + tabSpaces, str.toString());
                        default:              str.append(cbuf[i++]);
                    }
                }
            case'=':
                if(cbuf[i] == '=') {
                    i++;
                    return new Token(row, (i - rowStart) + tabSpaces, TokenType.EQUALS);
                } else return new Token(row, (i - rowStart) + tabSpaces, TokenType.EQSIGN);
            case'&':
                if(cbuf[i] == '&') {
                    i++;
                    return new Token(row, (i - rowStart) + tabSpaces, TokenType.AND);
                } else throw new RuntimeException("Error while lexing " + inputFile + " at row: " + row + " column: " + (i-rowStart) + tabSpaces);
            case'|':
                if(cbuf[i] == '|') {
                    i++;
                    return new Token(row, (i - rowStart) + tabSpaces, TokenType.OR);
                } else throw new RuntimeException("Error while lexing " + inputFile + " at row: " + row + " column: " + (i-rowStart) + tabSpaces);
            case'/':
                switch(cbuf[i]) {
                    case'/':
                        while(cbuf[i] != (char)0 && cbuf[i] != '\n') i++;
                        return next();
                    case'*':
                        while((cbuf[++i] != (char)0 && cbuf[i] != '*') || (cbuf[1 + 1] != (char)0 && cbuf[i + 1] != '/')) {
                            if(cbuf[i] == '\n') { row++; rowStart = i; }
                        }
                        i += 2;
                        return next();
                    default: return new Token(row, (i - rowStart) + tabSpaces, TokenType.DIV);
                }
            default: throw new RuntimeException("Error while lexing " + inputFile + " at row: " + row + " column: " + (i-rowStart) + tabSpaces);
        }
    }
}
