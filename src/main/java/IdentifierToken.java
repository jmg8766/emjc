public class IdentifierToken extends Token {
    String value;

    IdentifierToken(int row, int col, String value) { super(row, col, TokenType.ID); this.value = value; }
}
