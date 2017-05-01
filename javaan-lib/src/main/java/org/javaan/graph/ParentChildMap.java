package org.javaan.graph;

import java.util.*;

public class ParentChildMap<P, C> extends HashMap<P, List<C>> {

    public boolean addChild(P parent, C child) {
        List<C> children = get(parent);
        if (children == null) {
            children = new ArrayList<>();
            put(parent, children);
        }
        return children.add(child);
    }
}
