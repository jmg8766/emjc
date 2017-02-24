public class StringLiteralToken extends Token {
    String value;

    StringLiteralToken(int row, int col, String value) { super(row, col, TokenType.ID); this.value = value; }

    @Override
    public String toString() { return row + ":" + col + " " + type + "(" + value + ")"; }
}
