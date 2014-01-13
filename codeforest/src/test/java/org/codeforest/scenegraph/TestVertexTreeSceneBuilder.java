package org.codeforest.scenegraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

import org.codeforest.layout.BoxTreeLayout;
import org.codeforest.layout.SceneData;
import org.codeforest.layout.TreeWidthCalculator;
import org.codeforest.layout.VertexSceneContext;
import org.codeforest.scenegraph.EdgeNodeFactory;
import org.codeforest.scenegraph.VertexNodeFactory;
import org.codeforest.scenegraph.VertexTreeSceneBuilder;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

public class TestVertexTreeSceneBuilder {
	
	/*
	
	Structure of test graph:

     1 1 1 1  1 1  1  1
     H I J K  L M  N  O
      \|/   \/   \/   |
       3     2    2   1
       D     E    F   G
        \    |   /    |
         \   7  /     1
          ---B--      C
             \       /
              \  8  /
               --A--
 
	 */
	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";
	private static final String D = "D";
	private static final String E = "E";
	private static final String F = "F";
	private static final String G = "G";
	private static final String H = "H";
	private static final String I = "I";
	private static final String J = "J";
	private static final String K = "K";
	private static final String L = "L";
	private static final String M = "M";
	private static final String N = "N";
	private static final String O = "O";
	
	private void assertTranslation(SceneData sceneData, double x, double y, double z) {
		assertNotNull(sceneData);
		Node node = sceneData.getNode();
		assertNotNull(node);
		Transform3D t3d = new Transform3D();
		node.getLocalToVworld(t3d);
		Vector3d v = new Vector3d();
		t3d.get(v);
		assertEquals(x, v.x, 0);
		assertEquals(y, v.y, 0);
		assertEquals(z, v.z, 0);
	}

	@Test
	public void testCreateScene() {
		DirectedGraph<String, String> graph = new DefaultDirectedGraph<String, String>(new EdgeFactory<String, String>() {
			public String createEdge(String sourceVertex, String targetVertex) {
				return sourceVertex + targetVertex;
			};
		});
		graph.addVertex(A);
		graph.addVertex(B);
		graph.addVertex(C);
		graph.addVertex(D);
		graph.addVertex(E);
		graph.addVertex(F);
		graph.addVertex(G);
		graph.addVertex(H);
		graph.addVertex(I);
		graph.addVertex(J);
		graph.addVertex(K);
		graph.addVertex(L);
		graph.addVertex(M);
		graph.addVertex(N);
		graph.addVertex(O);
		graph.addEdge(A, B);
		graph.addEdge(A, C);
		graph.addEdge(B, D);
		graph.addEdge(B, E);
		graph.addEdge(B, F);
		graph.addEdge(C, G);
		graph.addEdge(D, H);
		graph.addEdge(D, I);
		graph.addEdge(D, J);
		graph.addEdge(E, K);
		graph.addEdge(E, L);
		graph.addEdge(F, M);
		graph.addEdge(F, N);
		graph.addEdge(G, O);
		
		VertexNodeFactory<String> shapeFactory = new VertexNodeFactory<String>() {
			public Node createNode(String vertex) {
				return new Shape3D();
			}
		};
		EdgeNodeFactory<String, String> edgeNodeFactory = new EdgeNodeFactory<String, String>() {
			public Node createNode(String edge, String source, String target,
					Vector3d startVector, Vector3d endVector) {
				return null;
			}
			
		};
		VertexSceneContext<String> context = new VertexSceneContext<String>();
		new TreeWidthCalculator<>(context, graph).calculateVertexWidth(A);
		VertexTreeSceneBuilder<String, String> sceneBuilder = new VertexTreeSceneBuilder<String, String>(context, graph, shapeFactory, edgeNodeFactory, new BoxTreeLayout<String>(context, 2d, 3d));
		sceneBuilder.createScene(A);
		
		assertTranslation(context.get(A), 0, 0, 0);
		assertTranslation(context.get(B), -1, 3, 0);
		assertTranslation(context.get(D), -5, 6, 0);
		assertTranslation(context.get(H), -7, 9, 0);
		
		assertTranslation(context.get(C), 7, 3, 0);
		assertTranslation(context.get(G), 7, 6, 0);
		assertTranslation(context.get(O), 7, 9, 0);
	}
}