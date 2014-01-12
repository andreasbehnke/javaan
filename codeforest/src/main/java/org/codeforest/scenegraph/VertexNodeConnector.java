package org.codeforest.scenegraph;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import org.codeforest.layout.VertexSceneContext;
import org.jgrapht.Graph;

/**
 * Connects vertex nodes based on the edges of a given graph
 */
public class VertexNodeConnector<V, E> {

	private final VertexSceneContext<V> context;
	
	private final EdgeNodeFactory<V, E> edgeFactory;
	
	public VertexNodeConnector(VertexSceneContext<V> context, EdgeNodeFactory<V, E> edgeFactory) {
		this.context = context;
		this.edgeFactory = edgeFactory;
	}
	
	private Vector3d getTranslationOfVertex(V vertex) {
		Node node = context.get(vertex).getNode();
		if (node == null) {
			System.out.println("Warning: missing node for vertex " + vertex);
			return new Vector3d();
		}
		Transform3D t3d = new Transform3D();
		node.getLocalToVworld(t3d);
		Vector3d v = new Vector3d();
		t3d.get(v);
		return v;
	}

	public TransformGroup createScene(Graph<V, E> graph) {
		TransformGroup transformGroup = new TransformGroup();
		for (E edge : graph.edgeSet()) {
			V source = graph.getEdgeSource(edge);
			V target = graph.getEdgeTarget(edge);
			Vector3d startVector = getTranslationOfVertex(source);
			Vector3d endVector = getTranslationOfVertex(target);
			transformGroup.addChild(edgeFactory.createNode(edge, source, target, startVector, endVector));
		}
		return transformGroup;
	}
}
