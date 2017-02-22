public class Token {
    public static final int
        BAD = 0, EOF = 1, COLON = 2, SEMICOLON = 3,
        DOT = 4, COMMA = 5, EQSIGN = 6, EQUALS = 7,
        BANG = 8, ID = 9, INT = 10
        ;

    int kind, value;
    StringBuffer id;

//    Token(int kind, StringBuffer id) {
//        this.kind = kind;
//        this.id= id;
//    }
}

