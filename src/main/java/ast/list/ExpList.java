package ast.list;

import ast.expression.Exp;

import java.util.Iterator;
import java.util.Vector;

public class ExpList {
	public Vector<Exp> list = new Vector();

	public boolean equals(Object obj) {
		if(obj instanceof ExpList && ((ExpList)obj).list.size() == list.size()){
			ExpList obj2= ((ExpList)obj);
			Iterator itr1 = list.iterator();
			Iterator itr2 = list.iterator();
			while(itr1.hasNext() && itr2.hasNext()) { if(!itr1.next().equals(itr2.next())) break;}
			return true;
		} return false;
	}

}
