package org.javaan.graph;

import java.util.*;

public class ParentChildMap<P, C> implements Map<P, List<C>> {

    private final Map<P, List<C>> internalMap;

    public ParentChildMap() {
        this.internalMap = new HashMap<>();
    }

    public ParentChildMap(Map<P, List<C>> map) {
        this.internalMap = map;
    }

    public boolean addChild(P parent, C child) {
        List<C> children = internalMap.get(parent);
        if (children == null) {
            children = new ArrayList<>();
            internalMap.put(parent, children);
        }
        return children.add(child);
    }

    public ParentChildMap<C, P> invers() {
        ParentChildMap<C, P> result = new ParentChildMap<>();
        for (Entry<P,List<C>> entry : entrySet()) {
            for (C child : entry.getValue()) {
                result.addChild(child, entry.getKey());
            }
        }
        return result;
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    @Override
    public List<C> get(Object key) {
        return internalMap.get(key);
    }

    @Override
    public List<C> put(P key, List<C> value) {
        return internalMap.put(key, value);
    }

    @Override
    public List<C> remove(Object key) {
        return internalMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends P, ? extends List<C>> m) {
        internalMap.putAll(m);
    }

    @Override
    public void clear() {
        internalMap.clear();
    }

    @Override
    public Set<P> keySet() {
        return internalMap.keySet();
    }

    @Override
    public Collection<List<C>> values() {
        return internalMap.values();
    }

    @Override
    public Set<Entry<P, List<C>>> entrySet() {
        return internalMap.entrySet();
    }
}
