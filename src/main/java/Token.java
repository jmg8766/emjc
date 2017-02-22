public class Token {

    public enum TokenType {
        COLON, SEMICOLON, DOT, COMMA, EQSIGN, EQUALS, BANG, LPAREN,
        RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE, AND, OR, LESSTHAN,
        PLUS, MINUS, TIMES, DIV, CLASS, PUBLIC, STATIC, VOID, STRING,
        EXTENDS, INT, BOOLEAN, WHILE, IF, ELSE, MAIN, RETURN, LENGTH,
        TRUE, FALSE, THIS, NEW, PRINTLN, SIDEF, ID, INTLIT, STRINGLIT;
    }

    private int row, col, val;
    TokenType t;
    String id;

    Token(int row, int col, TokenType t) {
        this.row = row;
        this.col = col;
        this.t = t;
    }

    Token(int row, int col, TokenType t, int val) {
        this(row, col, t);
        this.val = val;
    }

    Token(int row, int col, TokenType t, String id) {
        this(row, col, t);
        this.id = id;
    }

    @Override
    public String toString() {
        return row + ":" + col + " " + t + "(" + /*?????*/ ")";
    }

}

