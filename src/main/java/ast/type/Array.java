package ast.type;

public class Array<T extends Type> extends Type {
    public T[] val;

    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
