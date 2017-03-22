package ast.statement;

import ast.Tree;
import ast.Visitor;

/* Skip represents empty statement.
 * It is useful e.g. as "else" block of and if-then-else statement
 */
public class Skip extends Tree implements Statement {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
};