package ast.type;

public class String extends Type {

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
