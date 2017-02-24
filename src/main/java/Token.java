public class Token {
    int row, col;
    TokenType type;

    Token(int row, int col, TokenType t) { this.row = row; this.col = col; this.type = t; }

    @Override
    public String toString() { return row + ":" + col + " " + type + "()"; }
}

