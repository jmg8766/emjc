package symbol;

import ast.Binding;

public class HashT {
	Hashtable<>

	final int SIZE = 256;
	Bucket[] table = new Bucket[SIZE];

	public void insert(Symbol s, Binding b) {
		int index = s.hashCode() % SIZE;
		table[index] = new Bucket(s, b, table[index]);
	}

	public Binding lookup(Symbol s) {
		int index = s.hashCode() % SIZE;
		for(Bucket b = table[index]; b != null; b = b.next) {
			if(s.equals(b.key)) return b.binding;
		}
		return null;
	}

	void pop(Symbol s) {
		int index = s.hashCode() % SIZE;
	}
}

class Bucket {
	Symbol key;
	Binding binding;
	Bucket next;

	public Bucket(Symbol k, Binding b, Bucket n) {
		key = k;
		binding = b;
		next = n;
	}
}
