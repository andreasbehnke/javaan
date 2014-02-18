package org.javaan;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
				graphHandler = createGraphHandler();
				graphHandler.setLivePreview(true);
			}
		};
		getContentPane().add(graphComponent);
        
        // layout graph
        mxHierarchicalLayout layout = new mxHierarchicalLayout(view);
        layout.setInterRankCellSpacing(200d);
        layout.execute(view.getDefaultParent());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JFrame frame = this;
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		Settings.putGraph2dFrameLocationAndSize(frame);
        	}
		});
        Settings.setGraph2dFrameLocationAndSize(frame);
	}
}