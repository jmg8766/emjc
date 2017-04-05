package oldast.statement;

import oldast.Binding;
import oldast.ID;
import oldast.Tree;
import oldast.Visitor;
import oldast.type.Type;

public class VarDeclaration extends Tree implements Statement, Binding {
    public Type type;
    public ID id;

    public VarDeclaration(Type type, ID id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
