package oldast.type;

import oldast.Tree;
import oldast.Visitor;

public class String extends Tree implements Type {

    public String(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
