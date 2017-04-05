package ast.list;

import ast.statement.Statement;

import java.util.Vector;

public class StatementList {
	private Vector<Statement> list = new Vector();

	public void addElement(Statement n) {
		list.addElement(n);
	}

	public Statement elementAt(int i) {
		return list.elementAt(i);
	}

	public int size() {
		return list.size();
	}
}
