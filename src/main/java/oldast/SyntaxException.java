package ast;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String errorMessage) {
        super(errorMessage);
    }
}
