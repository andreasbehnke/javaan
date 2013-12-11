package org.javaan.model;

import org.javaan.graph.NamedObjectEdge;
import org.javaan.graph.NamedObjectVisitor;

public class InterfaceMethodFinder implements NamedObjectVisitor<Interface> {
	
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
	public void visit(Interface named, int level) {
		methodFound = context.getMethod(named, signature);
	}

	@Override
	public void visit(NamedObjectEdge<Interface> namedEdge, int level) {}

	public Method getMethodFound() {
		return methodFound;
	}
}
