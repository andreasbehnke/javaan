package org.javaan;

import javax.swing.JFrame;

import org.javaan.jgraphx.CellStyle;
import org.javaan.jgraphx.MxGraphBuilder;
import org.jgrapht.Graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Graph2dDisplay<V, E> extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public Graph2dDisplay(String title, Graph<V, E> input, CellStyle<V, E> cellStyle) {
		super(title);
		mxGraph view = new MxGraphBuilder<V, E>(input, cellStyle).build();
		
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
        layout.setInterRankCellSpacing(200d);
        //layout.setEdgeRouting(false);
        //layout.setDisableEdgeStyle(false);
        layout.execute(view.getDefaultParent());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        
        pack();
	}
}