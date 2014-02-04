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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.javaan.graph.GraphVisitor;
import org.javaan.graph.TraversalDirectedGraph;
import org.javaan.graph.UnsupportedEdgeFactory;
import org.javaan.graph.VertexEdgeDirectedGraph;
import org.javaan.graph.VertexEdgeGraphVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

/**
 * Represents the call-graph of all loaded methods.
 * Type dependencies are created for every method.
 */
public class CallGraph {

	private final VertexEdgeDirectedGraph<Method> callerOfMethod = new VertexEdgeDirectedGraph<Method>();

	private final TraversalDirectedGraph<Clazz, Dependency> usageOfClass = createDirectedGraph();
	
	private final TraversalDirectedGraph<Package, Dependency> usageOfPackage = createDirectedGraph();
			
	private final ClassContext classContext;
	
	private final MethodResolver methodResolver;
	
	private static <V, E> TraversalDirectedGraph<V, E> createDirectedGraph() {
		return new TraversalDirectedGraph<V, E>(
				new DefaultDirectedGraph<V, E>(
						new UnsupportedEdgeFactory<V, E>()));
	}

	public CallGraph(ClassContext classContext) {
		this.classContext = classContext;
		this.methodResolver = new ImplementationResolver(classContext);
	}
	
	public int size() {
		return callerOfMethod.vertexSet().size();
	}
	
	public VertexEdgeDirectedGraph<Method> getCallerOfMethodGraph() {
		return callerOfMethod;
	}
	
	public TraversalDirectedGraph<Clazz, Dependency> getUsageOfClassGraph() {
		return usageOfClass;
	}
	
	private void addUsageOfClass(Method caller, Method callee) {
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
		Dependency.addDependency(usageOfClass, classOfCaller, classOfCallee, callee);
	}
	
	private void addUsageOfPackage(Method caller, Method callee) {
		Package packageOfCaller = classContext.getPackageOfType(caller.getType());
		Package packageOfCallee = classContext.getPackageOfType(callee.getType());
		if (packageOfCaller.equals(packageOfCallee)) {
			return;
		}
		Dependency.addDependency(usageOfPackage, packageOfCaller, packageOfCallee, callee);
	}
	
	private void addCallInternal(Method caller, Method callee) {
		callerOfMethod.addEdge(caller, callee);
		addUsageOfClass(caller, callee);
		addUsageOfPackage(caller, callee);
	}

	public void addCall(Method caller, Method callee) {
		if (caller == null) {
			throw new IllegalArgumentException("Parameter caller must not be null");
		}
		if (callee == null) {
			throw new IllegalArgumentException("Parameter callee must not be null");
		}
		Set<Type> resolvedTypes = methodResolver.resolve(callee);
		for (Type type : resolvedTypes) {
			Method calleeCandidate = null;
			switch (type.getJavaType()) {
			case CLASS:
				calleeCandidate = classContext.getVirtualMethod((Clazz)type, callee.getSignature());
				break;
			case INTERFACE:
				calleeCandidate = classContext.getVirtualMethod((Interface)type, callee.getSignature());
				break;
			default:
				throw new IllegalArgumentException("Unknown java type:" + type.getJavaType());
			}
			if (calleeCandidate != null) {
				addCallInternal(caller, calleeCandidate);
			}
		}
	}
	
	// method calls
	
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
	
	// class usage

	public void traverseUsedTypes(Clazz using, GraphVisitor<Clazz, Dependency> usedVisitor) {
		usageOfClass.traverseSuccessorsDepthFirst(using, usedVisitor);
	}
	
	public void traverseUsingTypes(Clazz used, GraphVisitor<Clazz, Dependency> usingVisitor) {
		usageOfClass.traversePredecessorsDepthFirst(used, usingVisitor);
	}

	public Set<Clazz> getLeafUsedTypes(Clazz using) {
		return usageOfClass.getLeafSuccessors(using);
	}
	
	public Set<Clazz> getLeafUsingTypes(Clazz using) {
		return usageOfClass.getLeafPredecessors(using);
	}
	
	private static <V> DirectedSimpleCycles<V, ?> createCycleDetector(DirectedGraph<V, ?> graph) {
		return new JohnsonSimpleCycles<>(graph);
	}
	
	private static <V> List<List<V>> getDependencyCycles(DirectedGraph<V, ?> graph) {
		List<List<V>> cycles = new ArrayList<>();
		for (List<V> list : createCycleDetector(graph).findSimpleCycles()) {
			if (list.size() > 1) {
				cycles.add(list);
			}
		}
		return cycles;
	}
	
	/**
	 * @return list of types which take part in a using dependency cycle
	 */
	public List<List<Clazz>> getDependencyCycles() {
		return getDependencyCycles(usageOfClass);
	}
	
	private static <V, E> void traverseDepdendencyCycles(GraphVisitor<V, E> cyclesVisitor, DirectedGraph<V, E> graph) {
		StrongConnectivityInspector<V, E> inspector = new StrongConnectivityInspector<V, E>(graph);
		List<DirectedSubgraph<V, E>> cycleGraphs = inspector.stronglyConnectedSubgraphs();
		int index = 1;
		TraversalDirectedGraph<V, E> traversalGraph;
		for (DirectedSubgraph<V, E> subgraph : cycleGraphs) {
			if (subgraph.vertexSet().size() > 1) {// ignore dependency cycles within one vertex (these cycles have no impact in software design)
				traversalGraph = new TraversalDirectedGraph<V, E>(subgraph);
				cyclesVisitor.visitGraph(traversalGraph, index);
				traversalGraph.traverseDepthFirst(cyclesVisitor);
				index++;
			}
		}
	}
	
	public void traverseDependencyCycles(GraphVisitor<Clazz, Dependency> cyclesVisitor) {
		traverseDepdendencyCycles(cyclesVisitor, usageOfClass);
	}
	
	// package usage
	
	public void traverseUsedPackages(Package using, GraphVisitor<Package, Dependency> usedVisitor) {
		usageOfPackage.traverseSuccessorsDepthFirst(using, usedVisitor);
	}
	
	public void traverseUsingPackages(Package used, GraphVisitor<Package, Dependency> usingVisitor) {
		usageOfPackage.traversePredecessorsDepthFirst(used, usingVisitor);
	}

	/**
	 * For each type contained in package retrieves the leave used types.
	 * Returns set of packages of these leave types.
	 */
	public Set<Package> getLeafUsedPackages(Package using) {
		Set<Clazz> usedTypes = new HashSet<>();
		Set<Clazz> typesOfPackage = classContext.getClassesOfPackage(using);
		for (Clazz clazz : typesOfPackage) {
			usedTypes.addAll(getLeafUsedTypes(clazz));
		}
		Set<Package> usedPackages = new HashSet<>();
		for (Clazz clazz : usedTypes) {
			Package used = classContext.getPackageOfType(clazz);
			if (!used.equals(using)) {
				usedPackages.add(used);
			}
		}
		return usedPackages;
	}
	
	/**
	 * For each type contained in package retrieves the leave using types.
	 * Returns set of packages of these leave types.
	 */
	public Set<Package> getLeafUsingPackages(Package used) {
		Set<Clazz> usingTypes = new HashSet<>();
		Set<Clazz> typesOfPackage = classContext.getClassesOfPackage(used);
		for (Clazz clazz : typesOfPackage) {
			usingTypes.addAll(getLeafUsingTypes(clazz));
		}
		Set<Package> usingPackages = new HashSet<>();
		for (Clazz clazz : usingTypes) {
			Package using = classContext.getPackageOfType(clazz);
			if (!used.equals(using)) {
				usingPackages.add(using);
			}
		}
		return usingPackages;
	}
	
	/**
	 * @return list of packages which take part in a using dependency cycle
	 */
	public List<List<Package>> getPackageDependencyCycles() {
		return getDependencyCycles(usageOfPackage);
	}
	
	public void traversePackageDependencyCycles(GraphVisitor<Package, Dependency> cyclesVisitor) {
		traverseDepdendencyCycles(cyclesVisitor, usageOfPackage);
	}
	
	/**
	 * @return topological sorted list of packages. Because package dependency graph may contain
	 * cycles, the topological sort can not applied directly to that graph. Instead, the cycles are
	 * cut at the point of minimum count of dependencies between two packages before topological sort
	 * is applied.
	 */
	public List<Package> getTopologicalSortedPackages() {
		throw new NotImplementedException("implemented in feature branch!");
	}
}