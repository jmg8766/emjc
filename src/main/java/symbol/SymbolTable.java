package symbol;

import ast.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class SymbolTable<V> {

	private final LinkedList<HashMap<String, V>> symbols = new LinkedList<>();

	/**
	 * @param key
	 * @param value
	 * @return the previous value associated with the key
	 */
	public V put(Identifier key, V value) {
		return symbols.getFirst().put(key.s.intern(), value);
	}

	public V get(Identifier key) {
		return symbols.stream().map(m -> m.get(key.s.intern())).filter(Objects::nonNull).findFirst().orElseGet(() ->
				null);
	}

	public void beginScope() {
		symbols.addFirst(new HashMap<>());
	}

	public void endScope() {
		symbols.remove();
	}
}
