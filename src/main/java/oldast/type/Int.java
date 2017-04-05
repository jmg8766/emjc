package oldast.type;

import oldast.Tree;
import oldast.Visitor;

public class Int extends Tree implements Type {

	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
