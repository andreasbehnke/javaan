package org.javaan.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;

/**
 * Reads graphs from simple text files. Text files have the following simple syntax:
 * Lines beginning with # are comments.
 * Lines beginning with v or V define a vertex to be added, lines beginning with e or E define an edge:
 *
 * V vertexLabel
 * E targetVertex sourceVertex edgeLabel
 *
 * If an edge line references unknown vertices, these are added to the graph before adding the edge.
 * So the following produces a valid graph:
 *
 *  E A B AB
 */
public class SimpleGraphReader<V, E> {

	public interface ObjectProducer<V, E> {

		V createVertex(String vertexLabel);

		E createEdge(V source, V target, String edgeLabel);

	}

	private final Graph<V, E> target;

	private final Map<String, V> vertexMap = new HashMap<>();

	private final Map<String, E> edgeMap = new HashMap<>();

	private final ObjectProducer<V, E> objectProducer;

	public SimpleGraphReader(Graph<V, E> target, ObjectProducer<V, E> objectProducer) {
		this.target = target;
		this.objectProducer = objectProducer;
	}

	public Map<String, V> getVertexMap() {
		return vertexMap;
	}

	public Map<String, E> getEdgeMap() {
		return edgeMap;
	}

	private String[] skipComments(BufferedReader bufferedReader) throws IOException {
		String line = bufferedReader.readLine();
		while(line != null) {
			line = line.trim();
			if (!line.startsWith("#")) {
				return line.split("\\s+");
			}
			line = bufferedReader.readLine();
		}
		return null;
	}

	private V addVertex(String vertexLabel) {
		V vertex = objectProducer.createVertex(vertexLabel);
		vertexMap.put(vertexLabel, vertex);
		target.addVertex(vertex);
		return vertex;
	}

	private void addVertex(String[] columns) throws IOException {
		if (columns.length == 2) {
			String vertexLabel = columns[1];
			addVertex(vertexLabel);
		} else {
			throw new IOException("Vertex line does not contain one vertex label");
		}
	}

	private void addEdge(String sourceLabel, String targetLabel) {
		V sourceVertex = addVertex(sourceLabel);
		V targetVertex = addVertex(targetLabel);
		E edge = target.addEdge(sourceVertex, targetVertex);
		edgeMap.put(edge.toString(), edge);
	}

	private void addEdge(String sourceLabel, String targetLabel, String edgeLabel) {
		V sourceVertex = addVertex(sourceLabel);
		V targetVertex = addVertex(targetLabel);
		E edge = objectProducer.createEdge(sourceVertex, targetVertex, edgeLabel);
		target.addEdge(sourceVertex, targetVertex, edge);
		edgeMap.put(edge.toString(), edge);
	}

	private void addEdge(String[] columns) throws IOException {
		String sourceLabel, targetLabel, edgeLabel;
		switch (columns.length) {
		case 3:
			sourceLabel = columns[1];
			targetLabel = columns[2];
			addEdge(sourceLabel, targetLabel);
			break;
		case 4:
			sourceLabel = columns[1];
			targetLabel = columns[2];
			edgeLabel = columns[3];
			addEdge(sourceLabel, targetLabel, edgeLabel);
			break;
		default:
			throw new IOException("Edge line does not contain two vertex label and an optional edge name");
		}
	}

	public Graph<V, E> readGraph(Reader reader) throws IOException {
		BufferedReader bufferedReader = (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
		String[] columns = skipComments(bufferedReader);
		while(columns != null) {
			if (columns.length > 0) {
				String command = columns[0];
				switch (command) {
				case "v":
				case "V":
					addVertex(columns);
					break;
				case "e":
				case "E":
					addEdge(columns);
					break;
				case "":
					break;
				default:
					throw new IOException("Line does not start with v,V,e,E or #");
				}
			}
			columns = skipComments(bufferedReader);
		}
		return target;
	}
}
