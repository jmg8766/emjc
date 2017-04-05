package symbol;

import java.util.HashMap;

public class Symbol {
	private static final HashMap<String, Symbol> dictionary = new HashMap<>();
	private final String name;

	private Symbol(String n) {
		name = n;
	}

	public static Symbol symbol(String n) {
		return dictionary.computeIfAbsent(n.intern(), Symbol::new);
	}

	public String toString() {
		return name;
	}
}
