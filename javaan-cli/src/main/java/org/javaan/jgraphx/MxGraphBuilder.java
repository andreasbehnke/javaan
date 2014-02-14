package org.javaan.jgraphx;

import java.util.HashMap;
import java.util.Map;

import org.javaan.print.ObjectFormatter;
import org.jgrapht.Graph;

import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

public class MxGraphBuilder<V, E> {
	
	private final Graph<V, E> source;
	
	private Map<V, mxICell> vertexToCell = new HashMap<V, mxICell>();
    
    private final String edgeStyle;
    
    private final String vertexStyle;
    
    private final ObjectFormatter<E> edgeFormatter;
    
    private final ObjectFormatter<V> vertexFormatter;
    
    private mxGraph target;
 
	public MxGraphBuilder(Graph<V, E> source, String vertexStyle, String edgeStyle, 
			ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter) {
		this.source = source;
		this.edgeStyle = edgeStyle;
		this.vertexStyle = vertexStyle;
		this.edgeFormatter = edgeFormatter;
		this.vertexFormatter = vertexFormatter;
	}

	private void addVertices() {
		for (V vertex : source.vertexSet()) {
	        mxICell cell = (mxICell)target.insertVertex(null, null, vertexFormatter.format(vertex), 0, 0, 0, 0, vertexStyle);
	        target.updateCellSize(cell);
	        vertexToCell.put(vertex, cell);
		}
	}
	
	private void addEdges() {
		for (E edge : source.edgeSet()) {
			V sourceVertex = source.getEdgeSource(edge);
            V targetVertex = source.getEdgeTarget(edge);

            Object sourceCell = vertexToCell.get(sourceVertex);
            Object targetCell = vertexToCell.get(targetVertex);
            
            if (sourceCell != null && targetCell != null)
            {
            	// calculate edge width using logarithm
            	double edgeWeight = source.getEdgeWeight(edge);
            	double edgeWidth = Math.log1p(edgeWeight);
            	mxICell cell = (mxICell)target.insertEdge(null, null, null, sourceCell, targetCell, edgeStyle + "strokeWidth=" + Math.round(edgeWidth) + ";");
            	target.updateCellSize(cell);
            }
		}
	}
	
	public mxGraph build() {
		target = new mxGraph();
		target.getModel().beginUpdate();
		try {
			addVertices();
			addEdges();
		} finally {
			target.getModel().endUpdate();
		}
		return target;
	}
}
