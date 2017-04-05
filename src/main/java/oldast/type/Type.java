package oldast.type;

import oldast.Visitor;

public interface Type {

	<R> R accept(Visitor<R> v);
}
