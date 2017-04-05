package ast.list;

import java.util.Vector;

public class ExpList {
	private Vector<ClassDecl> list = new Vector();

	public void addElement(ClassDecl n) {
		list.addElement(n);
	}

	public ClassDecl elementAt(int i) {
		return list.elementAt(i);
	}

	public int size() {
		return list.size();
	}
}
