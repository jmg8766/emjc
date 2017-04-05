package symbol;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Table<R> {
	private final HashMap<Symbol, LinkedList<R>> table = new HashMap<>();
	private final Stack<Symbol> scope = new Stack<>();

	public void put(String key, R value) {
		Symbol s = Symbol.symbol(key);
		table.computeIfAbsent(s, k -> new LinkedList());
		table.get(s).add(value);
		scope.push(s);
	}

	public R get(String key) {
		return table.get(Symbol.symbol(key)).peek();
	}

	public boolean contains(String key) {
		LinkedList<R> l = table.get(Symbol.symbol(key));
		return l != null && l.contains(Symbol.symbol(key));
	}

	public void beginScope() {
		scope.push(null);
	}

	public void endScope() {
		Symbol s;
		while ((s = scope.pop()) != null) table.get(s).pop();
	}

}
