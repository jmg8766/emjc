package symbol;

import ast.Identifier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class SymbolTable<V> {
	int a;

	private final LinkedList<HashMap<String, V>> inScope = new LinkedList<>();

	public SymbolTable(){
		inScope.addFirst(new HashMap<String, V>());
	}
	/**
	 * @return the previous value associated with the key
	 */
	public V put(Identifier key, V value) {
		return inScope.getFirst().put(key.s.intern(), value);
	}

	/**
	 * @return the value associated with a key, or null if the key doesn't exist
	 */
	public V get(Identifier key) {
		return inScope.stream()
			.map(m -> m.get(key.s.intern()))
			.filter(Objects::nonNull)
			.findFirst()
			.orElseGet(() -> null);
	}

	public void beginScope() {
		inScope.addFirst(new HashMap<>());
	}

	public void endScope() {
		inScope.remove();
	}
}
