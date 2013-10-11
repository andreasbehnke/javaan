package org.javaan;

import java.util.Map;
import java.util.Set;

/**
 * Represents the call-graph for all methods of a library set.
 * Provides methods for querying call-graph.
 */
public class CallGraph {

	/**
	 * Stores the caller signatures for a given (key) method signature
	 */
	private final Map<String, Set<String>> callersMap;

	public CallGraph(final Map<String, Set<String>> callersMap) {
		this.callersMap = callersMap;
	}
	
	public Set<String> getCallers(String callee	) {
		return null;
	}
	
	public boolean containsMethod(String method) {
		return false;
	}
	
	public Set<String> getCallingEntryMethods(String callee) {
		return null;
	}
}
