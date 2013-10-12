package org.javaan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a digraph without multi-edges.  
 * Provides methods for querying graph.
 */
public class Graph {

	/**
	 * Stores the graphs parent child relations
	 */
	private final Map<String, Set<String>> nodeMap = new HashMap<String, Set<String>>();

	public void add(String method) {
		nodeMap.put(method, new HashSet<String>());
	}
	
	public void addCaller(String callee, String caller) {
		if (containsMethod(callee)) {
			nodeMap.get(callee).add(caller);
		} else {
			Set<String> callers = new HashSet<String>();
			callers.add(caller);
			nodeMap.put(callee, callers);
		}
		if (!containsMethod(caller)) {
			add(caller);
		}
	}
	
	public Set<String> getCallers(String callee	) {
		return nodeMap.get(callee);
	}
	
	public boolean hasCallers(String callee) {
		return nodeMap.get(callee).size() > 0;
	}
	
	public boolean containsMethod(String method) {
		return nodeMap.containsKey(method);
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
