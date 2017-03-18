import Tokens.Token;
import Tokens.TokenType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class LexerTest {

    Random random = new Random();
    TokenType[] types = TokenType.values();

    @DataProvider
    public Iterator<Object[]> benchmarkFiles() {
        return Arrays
                .stream(new File("src/test/benchmarks").listFiles())
                .filter(f -> f.getName().endsWith(".emj"))
                .map(f -> new Object[] {f})
                .iterator();
    }

    @Test(dataProvider = "benchmarkFiles")
    public void testGenLexFile(File f) throws IOException{
        System.out.println("Generating lex file for " + f.getName());
        // test if the file can be lexed without any errors
        new Lexer(f.getPath()).genLexFile();
    }

    @DataProvider
    public Iterator<Object[]> randomlyGeneratedFiles() throws IOException {
        return Stream.generate(() -> {
            StringBuilder input = new StringBuilder();
            StringBuilder expectedOutput = new StringBuilder();
            int col=1, row=1;
            do {
                do {
                    // insert random spaces/tabs/inline comments
                    // generate token
                    // insert random spaces/tabs/inline comments
                } while(++col < random.nextInt(1000));
                // append new line
                input.append('\n');
            } while(++row < random.nextInt(1000));
            input.append((char)0 + '\n');
            expectedOutput.append(new Token(row, col, TokenType.EOF).toString());
            return new Object[] {"", ""};
        }).iterator();
    }

    @Test(dataProvider="randomlyGeneratedFiles", enabled = false)
    public void testRandomlyGeneratedFile(String input, String expectedOuput) {
        //TODO
    }

    @Test
    public void testNext() {
    }
}