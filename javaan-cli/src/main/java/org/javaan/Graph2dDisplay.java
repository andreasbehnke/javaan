package org.javaan;

import javax.swing.JFrame;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class Graph2dDisplay extends JFrame {

	private static final long serialVersionUID = 1L;

	public Graph2dDisplay(Graph<?, ?> input, String title) {
		super(title);
		mxGraph view = new JGraphXAdapter<>(input);
		
		mxGraphComponent graphComponent = new mxGraphComponent(view);
        getContentPane().add(graphComponent);
        
        // layout graph
        mxIGraphLayout layout = new mxHierarchicalLayout(view);
        layout.execute(view.getDefaultParent());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        
        pack();
	}
}