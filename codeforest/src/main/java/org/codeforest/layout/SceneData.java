package org.codeforest.layout;

import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;

import org.codeforest.scenegraph.TableLayout;
import org.codeforest.scenegraph.VertexTreeSceneBuilder;

/**
 * Holds scene graph layout information of a vertex
 */
public class SceneData {

	private TransformGroup transformGroup;
	
	private Node node;
	
	private int subTreeWidth = -1;

	private int row = -1;

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
	 * Used by {@link VertexTreeSceneBuilder} to calculate tree layout.
	 */
	public int getSubTreeWidth() {
		return subTreeWidth;
	}

	public void setSubTreeWidth(int subTreeWidth) {
		this.subTreeWidth = subTreeWidth;
	}

	/**
	 * The row to place this vertex.
	 * Returns -1 if table position has not been calculated yet.
	 * Used by {@link TableLayout} for positioning vertex node.
	 */

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
