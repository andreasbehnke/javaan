package org.javaan.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ParentChildMap<P, C> extends HashMap<P, Set<C>> {

    public boolean addChild(P parent, C child) {
        Set<C> children = get(parent);
        if (children == null) {
            children = new HashSet<>();
            put(parent, children);
        }
        return children.add(child);
    }
}
