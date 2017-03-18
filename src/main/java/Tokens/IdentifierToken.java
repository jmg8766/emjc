package Tokens;

import Tokens.Token;
import Tokens.TokenType;

public class IdentifierToken extends Token {
    public String value;

    public IdentifierToken(int row, int col, String value) {
        super(row, col, TokenType.ID);
        this.value = value;
    }

    @Override
    public String toString() {
        return row + ":" + col + " " + type + "(" + value + ")";
    }
}
