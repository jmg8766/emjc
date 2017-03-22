package ast.statement;

import ast.Visitor;

public interface Statement {
	<R> R accept(Visitor<R> v);
}
