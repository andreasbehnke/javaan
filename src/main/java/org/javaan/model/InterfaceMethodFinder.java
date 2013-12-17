package org.javaan.model;

import org.javaan.graph.GraphVisitorAdapter;
import org.javaan.graph.VertexEdge;
import org.javaan.graph.VertexEdgeGraphVisitor;

public class InterfaceMethodFinder extends GraphVisitorAdapter<Interface, VertexEdge<Interface>> implements VertexEdgeGraphVisitor<Interface> {
	
	private final String signature;
	
	private final ClassContext context;
	
	private Method methodFound = null;

	public InterfaceMethodFinder(ClassContext context, String signature) {
		this.context = context;
		this.signature = signature;
	}

	@Override
	public boolean finished() {
		return methodFound != null;
	}

	@Override
	public void visitVertex(Interface named, int level) {
		methodFound = context.getMethod(named, signature);
	}

	public Method getMethodFound() {
		return methodFound;
	}
}
