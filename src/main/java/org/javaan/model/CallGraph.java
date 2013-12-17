package org.javaan.model;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javaan.graph.ExternalEdgeDirectedGraph;
import org.javaan.graph.GraphVisitor;
import org.javaan.graph.VertexEdgeDirectedGraph;
import org.javaan.graph.VertexEdgeGraphVisitor;
import org.jgrapht.alg.StrongConnectivityInspector;

/**
 * Represents the call-graph of all loaded methods.
 * Type dependencies are created for every method.
 */
public class CallGraph {

	private final VertexEdgeDirectedGraph<Method> callerOfMethod = new VertexEdgeDirectedGraph<Method>();

	private final ExternalEdgeDirectedGraph<Clazz, Method> usageOfClass = new ExternalEdgeDirectedGraph<Clazz, Method>();

	private final ClassContext classContext;

	public CallGraph(ClassContext classContext) {
		this.classContext = classContext;
	}

	public int size() {
		return callerOfMethod.vertexSet().size();
	}

	public void addCall(Method caller, Method callee) {
		if (caller == null) {
			throw new IllegalArgumentException("Parameter caller must not be null");
		}
		if (callee == null) {
			throw new IllegalArgumentException("Parameter callee must not be null");
		}
		callerOfMethod.addEdge(caller, callee);
		Clazz classOfCaller = (Clazz)caller.getType();
		Clazz classOfCallee = (Clazz)callee.getType();
		if (classOfCallee.equals(classOfCaller)) {
			return;
		}	
		if (classContext.getSpecializationsOfClass(classOfCallee).contains(classOfCaller)) {
			return;
		}
		if (classContext.getSpecializationsOfClass(classOfCaller).contains(classOfCallee)) {
			return;
		}
		usageOfClass.addEdge(classOfCaller, classOfCallee, callee);
	}
	
	public Set<Method> getCallers(Method callee) {
		return callerOfMethod.sourceVerticesOf(callee);
	}
	
	public Set<Method> getCallees(Method caller) {
		return callerOfMethod.targetVerticesOf(caller);
	}
	
	public void traverseCallers(Method callee, VertexEdgeGraphVisitor<Method> callerVisitor) {
		callerOfMethod.traversePredecessorsDepthFirst(callee, callerVisitor);
	}
	
	public void traverseCallees(Method caller, VertexEdgeGraphVisitor<Method> calleeVisitor) {
		callerOfMethod.traverseSuccessorsDepthFirst(caller, calleeVisitor);
	}
	
	public Set<Method> getLeafCallers(Method callee) {
		return callerOfMethod.getLeafPredecessors(callee);
	}
	
	public Set<Method> getLeafCallees(Method caller) {
		return callerOfMethod.getLeafSuccessors(caller);
	}

	public void traverseUsedTypes(Clazz using, GraphVisitor<Clazz, Method> usedVisitor) {
		usageOfClass.traverseSuccessorsDepthFirst(using, usedVisitor);
	}
	
	public void traverseUsingTypes(Clazz used, GraphVisitor<Clazz, Method> usingVisitor) {
		usageOfClass.traversePredecessorsDepthFirst(used, usingVisitor);
	}

	public Set<Clazz> getLeafUsedTypes(Clazz using) {
		return usageOfClass.getLeafSuccessors(using);
	}
	
	public Set<Clazz> getLeafUsingTypes(Clazz using) {
		return usageOfClass.getLeafPredecessors(using);
	}
	
	/**
	 * @return list of type sets which take part in a using dependency cycle
	 */
	public List<Set<Clazz>> getDependencyCycles() {
		StrongConnectivityInspector<Clazz, Method> inspector = new StrongConnectivityInspector<Clazz, Method>(usageOfClass);
		List<Set<Clazz>> cycles = new ArrayList<Set<Clazz>>();
		for (Set<Clazz> cycle : inspector.stronglyConnectedSets()) {
			if (cycle.size() > 1) { // ignore dependency cycles within one class (these cycles have no impact in software design)
				cycles.add(cycle);
			}
		}
		return cycles;
	}
}