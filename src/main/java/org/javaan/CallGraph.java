package org.javaan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Represents the call-graph for all methods of a library set.
 * Provides methods for querying call-graph.
 */
public class CallGraph {

	/**
	 * Stores the caller signatures for a given (key) method signature
	 */
	private final Map<String, Set<String>> callersMap = new HashMap<String, Set<String>>();

	public void add(String method) {
		callersMap.put(method, new HashSet<String>());
	}
	
	public void addCaller(String callee, String caller) {
		if (containsMethod(callee)) {
			callersMap.get(callee).add(caller);
		} else {
			Set<String> callers = new HashSet<String>();
			callers.add(caller);
			callersMap.put(callee, callers);
		}
		if (!containsMethod(caller)) {
			add(caller);
		}
	}
	
	public Set<String> getCallers(String callee	) {
		return callersMap.get(callee);
	}
	
	public boolean hasCallers(String callee) {
		return callersMap.get(callee).size() > 0;
	}
	
	public boolean containsMethod(String method) {
		return callersMap.containsKey(method);
	}
	
	public Set<String> getCallingEntryMethods(String callee) {
		Set<String> callingEntryMethods = new HashSet<String>();
		Stack<String> callers = new Stack<String>();
		callers.addAll(getCallers(callee));
		while(!callers.isEmpty()) {
			String caller = callers.pop();
			// detect cycle, ignore callee
			if (!caller.equals(callee)) {
				Set<String> callerCallers = getCallers(caller);
				if (callerCallers.size() > 0) {
					// more callers to detect
					callers.addAll(callerCallers);
				} else {
					// entry method found
					callingEntryMethods.add(caller);
				}
			}
		}
		return callingEntryMethods;
	}
}
