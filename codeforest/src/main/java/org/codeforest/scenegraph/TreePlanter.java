package org.codeforest.scenegraph;

import java.util.Collection;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import org.codeforest.model.VertexSceneContext;

/**
 * Plants trees created by {@link VertexTreeSceneBuilder} using a given layout.
 */
public class TreePlanter<V> {

	private final VertexTreeSceneBuilder<V, ?> treeBuilder;
	
	private final Layout<V> layout;
	
	public TreePlanter(VertexSceneContext<V> context, VertexTreeSceneBuilder<V, ?> treeBuilder, Layout<V> layout) {
		this.treeBuilder = treeBuilder;
		this.layout = layout;
	}

	public TransformGroup createScene(Collection<V> vertices) {
		TransformGroup transformGroup = new TransformGroup();
		layout.start();
		for (V vertex : vertices) {
			TransformGroup treeTransformGroup = treeBuilder.createScene(vertex);
			Vector3d translation = layout.getPosition(vertex);
			Transform3D transform3d = new Transform3D();
			transform3d.set(translation);
			treeTransformGroup.setTransform(transform3d);
			transformGroup.addChild(treeTransformGroup);
		}
		return transformGroup;
	}
}
