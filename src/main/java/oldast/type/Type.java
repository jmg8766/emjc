package ast.type;

import ast.Visitor;

public interface Type {

	<R> R accept(Visitor<R> v);
}
