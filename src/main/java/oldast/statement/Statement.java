package oldast.statement;

import oldast.Visitor;

public interface Statement {
	<R> R accept(Visitor<R> v);
}
