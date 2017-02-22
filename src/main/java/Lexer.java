package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Lexer {

    private static class Char {
        int row, col;
        char c;
        Char(int row, int col, char c) { this.row = row; this.col = col; this.c = c; }
    }

    enum TokenType {
        COLON, SEMICOLON, IDENTIFIER,
    }

    static class Token {
        int row, col, val;
        TokenType t;
        String value;

        @Override
        public String toString() {
            return row + ":" + col + " " + t + "(" + value +")";
        }
    }

    ConcurrentLinkedQueue<Char> chars = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Token> tokens = new ConcurrentLinkedQueue<>();

    AtomicBoolean allCharactersTokenized = new AtomicBoolean(false);
    String fileName;

    Lexer(String fileName) {
        this.fileName = fileName;
        AtomicBoolean allCharactersRead = new AtomicBoolean(false);
        new Thread(() -> {
            try(BufferedReader b = Files.newBufferedReader(Paths.get(fileName))) {

                // char stream logic

            } catch(IOException e) {}
            allCharactersRead.set(true);
        }).start();

        new Thread(() -> {
            Char c;
            while((c = chars.poll()) != null || allCharactersRead.get()) {

                // tokenize logic
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
