package org.javaan.jgraphx;

import org.javaan.model.Clazz;
import org.javaan.model.Dependency;
import org.javaan.model.Interface;
import org.javaan.print.ObjectFormatter;
import org.jgrapht.Graph;

public class DependencyGraphCellStyle<T> implements CellStyle<T, Dependency> {
	
	private static final String PACKAGE_COLOR = "0061b4f4";
	
	private static final String CLASS_COLOR = "009cf461";
	
	private static final String INTERFACE_COLOR = "00fff553";
	
	private static final String TYPE_STYLE = "editable=0;strokeColor=004682b1;fontColor=00004e8b;";

	private static final String EDGE_STYLE = "movable=0;editable=0;strokeColor=004682b1;fontColor=00004e8b;labelBackgroundColor=00eeeeee;labelBorderColor=004682b1;";
	
	private final ObjectFormatter<T> typeFormatter;
	
	private final ObjectFormatter<Dependency> dependencyFormatter;

	public DependencyGraphCellStyle(ObjectFormatter<T> typeFormatter, ObjectFormatter<Dependency> dependencyFormatter) {
		this.typeFormatter = typeFormatter;
		this.dependencyFormatter = dependencyFormatter;
	}

	@Override
	public String getEdgeLabel(Graph<T, Dependency> g, Dependency edge) {
		return dependencyFormatter.format(edge);
	}

	@Override
	public String getEdgeStyle(Graph<T, Dependency> g, Dependency edge) {
		// calculate edge width using logarithm
    	double edgeWeight = g.getEdgeWeight(edge);
    	double edgeWidth = Math.log1p(edgeWeight);
    	return EDGE_STYLE + "strokeWidth=" + Math.round(edgeWidth) + ";";
	}

	@Override
	public String getVertexLabel(Graph<T, Dependency> g, T vertex) {
		return typeFormatter.format(vertex);
	}

	@Override
	public String getVertexStyle(Graph<T, Dependency> g, T value) {
		String color = PACKAGE_COLOR;
		if (value instanceof Clazz) {
			color = CLASS_COLOR;
		} else if (value instanceof Interface) {
			color = INTERFACE_COLOR;
		}
		return TYPE_STYLE + "fillColor=" + color + ";";
	}

}
