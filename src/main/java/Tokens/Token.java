package Tokens;

import Tokens.TokenType;

public class Token {

    public int row, col;
    public TokenType type;

    public Token(int row, int col, TokenType t) {
        this.row = row;
        this.col = col;
        this.type = t;
    }

    @Override
    public String toString() {
        return row + ":" + col + " " + type + "()";
    }

}

