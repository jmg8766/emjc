package token;

public class StringLiteralToken extends Token {
    public String value;

    public StringLiteralToken(int row, int col, String value) { super(row, col, TokenType.STRINGLIT); this.value = value; }

    @Override
    public String toString() { return row + ":" + col + " " + type + "(" + value + ")"; }
}
