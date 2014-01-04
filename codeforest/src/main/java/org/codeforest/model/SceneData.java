package org.codeforest.model;

import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;

/**
 * Holds scene graph references of a vertex
 */
public class SceneData {

	private TransformGroup transformGroup;
	
	private Node node;
	
	private int subTreeWidth = -1;

	/**
	 * Reference to the transform group of vertex
	 */
	public TransformGroup getTransformGroup() {
		return transformGroup;
	}

	public void setTransformGroup(TransformGroup transformGroup) {
		this.transformGroup = transformGroup;
	}

	/**
	 * Reference to the scene graph node of vertex
	 */
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * The width of the subtree starting at this vertex.
	 * Returns -1 if tree width has not been calculated yet.
	 */
	public int getSubTreeWidth() {
		return subTreeWidth;
	}

	public void setSubTreeWidth(int subTreeWidth) {
		this.subTreeWidth = subTreeWidth;
	}
}
