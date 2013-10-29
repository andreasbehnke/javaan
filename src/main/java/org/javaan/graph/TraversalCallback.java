package org.javaan.graph;

import java.util.List;

public interface TraversalCallback<N> {

	List<N> getNextForTranversal(N node);
}
