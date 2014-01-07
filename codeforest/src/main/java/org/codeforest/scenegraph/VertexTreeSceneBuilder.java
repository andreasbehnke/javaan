package org.codeforest.scenegraph;

import java.util.Set;
import java.util.Stack;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import org.codeforest.model.SceneData;
import org.codeforest.model.VertexSceneContext;
import org.javaan.graph.TraversalDirectedGraph;
import org.jgrapht.DirectedGraph;

/**
 * Creates a 2 dimensional tree representation of a directed graph, following the
 * outgoing edges starting at a given start vertex. This class is not thread safe but
 * may be used for scene creation of multiple start vertices.
 */
public class VertexTreeSceneBuilder<V, E> {

	private final TraversalDirectedGraph<V, E> graph;

	private final VertexSceneContext<V> context;

	private final NodeFactory<V, E> nodeFactory;
	
	private final double vertexWidth;
	
	private final double vertexHeight;

	public VertexTreeSceneBuilder(VertexSceneContext<V> context, DirectedGraph<V, E> graph, NodeFactory<V, E> nodeFactory, double vertexWidth, double vertexHeight) {
		this.graph = new TraversalDirectedGraph<V, E>(graph);
		this.context = context;
		this.nodeFactory = nodeFactory;
		this.vertexWidth = vertexWidth;
		this.vertexHeight = vertexHeight;
	}

	private double getVertexWidth(V vertex) {
		return context.get(vertex).getSubTreeWidth() * vertexWidth;
	}

	private void createCrotch(TransformGroup transformGroup, V vertex, Set<V> targetVertices) {
		double x = - 0.5 * getVertexWidth(vertex);
		SceneData sceneData = context.get(vertex);
		Node node = nodeFactory.createNode(vertex);
		transformGroup.addChild(node);
		sceneData.setNode(node);
		Vector3d sourceVector = new Vector3d();
		for (V targetVertex : targetVertices) {
			double halfTargetWidth = getVertexWidth(targetVertex) * 0.5d;
			x += halfTargetWidth;
			Transform3D t3d = new Transform3D();
			Vector3d targetVector = new Vector3d(x, vertexHeight, 0);
			t3d.set(targetVector);
			TransformGroup targetTransformGroup = new TransformGroup(t3d);
			context.get(targetVertex).setTransformGroup(targetTransformGroup);
			transformGroup.addChild(targetTransformGroup);
			// create edge
			E edge = graph.getEdge(vertex, targetVertex);
			Node edgeNode = nodeFactory.createNode(edge, vertex, targetVertex, sourceVector, targetVector);
			transformGroup.addChild(edgeNode);
			x += halfTargetWidth;
		}
	}

	public TransformGroup createScene(V startVertex) {
		TransformGroup transformGroup = context.get(startVertex).getTransformGroup();
		if (transformGroup != null) {
			// scene for this vertex has been build before, just return it
			return transformGroup;
		}
		// calculate vertex widths for subgraph
		new TreeWidthCalculator<V, E>(context, graph).calculateVertexWidth(startVertex);
		transformGroup = new TransformGroup();
		context.get(startVertex).setTransformGroup(transformGroup);
		Stack<V> stack = new Stack<V>();
		stack.add(startVertex);
		while (!stack.empty()) {
			V vertex = stack.pop();
			Set<V> targetVertices = graph.targetVerticesOf(vertex);
			createCrotch(context.get(vertex).getTransformGroup(), vertex, targetVertices);
			stack.addAll(targetVertices);
		}	
		return transformGroup;
	}
}