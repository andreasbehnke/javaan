package org.javaan.jgraphx;

import java.util.HashMap;
import java.util.Map;

import org.javaan.model.GraphView;

import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

public class MxGraphBuilder<V, E> {
	
	private final GraphView<V, E> source;
	
	private Map<V, mxICell> vertexToCell = new HashMap<V, mxICell>();
    
    private final CellStyle<V, E> cellStyle;
	
    private mxGraph target;
 
	public MxGraphBuilder(GraphView<V, E> source, CellStyle<V, E> cellStyle) {
		this.source = source;
		this.cellStyle = cellStyle;
	}

	private void addVertices() {
		for (V vertex : source.vertexSet()) {
	        mxICell cell = (mxICell)target.insertVertex(null, null, cellStyle.getVertexLabel(source, vertex), 0, 0, 0, 0, cellStyle.getVertexStyle(source, vertex));
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
            	mxICell cell = (mxICell)target.insertEdge(null, null, cellStyle.getEdgeLabel(source, edge), sourceCell, targetCell, cellStyle.getEdgeStyle(source, edge));
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
