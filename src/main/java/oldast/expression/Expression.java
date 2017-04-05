package oldast.expression;

import oldast.Visitor;

public interface Expression {
	<R> R accept(Visitor<R> v);
}
