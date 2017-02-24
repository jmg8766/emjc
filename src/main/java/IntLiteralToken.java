public class IntLiteralToken extends Token {
    int value;
    IntLiteralToken(int row, int col, int value) { super(row, col, TokenType.INTLIT); this.value = value; }


    @Override
    public String toString() { return row + ":" + col + " " + type + "(" + value + ")";}
}
