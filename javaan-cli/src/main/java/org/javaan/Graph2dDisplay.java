package org.javaan;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import org.javaan.jgraphx.MxGraphBuilder;
import org.javaan.print.ObjectFormatter;
import org.jgrapht.Graph;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Graph2dDisplay<V, E> extends JFrame {
	
	private static final String VERTEX_STYLE = "editable=0;";
	
	private static final String EDGE_STYLE = "movable=0;editable=0;";

	private static final long serialVersionUID = 1L;

	public Graph2dDisplay(String title, Graph<V, E> input, ObjectFormatter<V> vertexFormatter, ObjectFormatter<E> edgeFormatter) {
		super(title);
		mxGraph view = new MxGraphBuilder<V, E>(input, VERTEX_STYLE, EDGE_STYLE, vertexFormatter, edgeFormatter).build();
		
		mxGraphComponent graphComponent = new mxGraphComponent(view) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void createHandlers() {
				//setTransferHandler(createTransferHandler());
				//panningHandler = createPanningHandler();
				//selectionCellsHandler = createSelectionCellsHandler();
				//connectionHandler = createConnectionHandler();
				graphHandler = createGraphHandler();
				graphHandler.setLivePreview(true);
			}
		};
        getContentPane().add(graphComponent);
        
        // layout graph
        //mxOrganicLayout layout = new mxOrganicLayout(view);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(view);
        layout.setInterRankCellSpacing(100d);
        //layout.setEdgeRouting(false);
        //layout.setDisableEdgeStyle(false);
        layout.execute(view.getDefaultParent());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        
        pack();
	}
}