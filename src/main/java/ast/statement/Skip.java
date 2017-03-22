package ast.statement;

/* Skip represents empty statement.
 * It is useful e.g. as "else" block of and if-then-else statement
 */
public class Skip extends Statement {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
};