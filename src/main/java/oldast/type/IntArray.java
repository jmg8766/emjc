package oldast.type;

import oldast.Tree;
import oldast.Visitor;

public class IntArray extends Tree implements Type {

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}
}
