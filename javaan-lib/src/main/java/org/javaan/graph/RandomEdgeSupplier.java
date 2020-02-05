package org.javaan.graph;

import java.util.UUID;
import java.util.function.Supplier;

public class RandomEdgeSupplier implements Supplier<String> {

    private final String prefix;

    public RandomEdgeSupplier(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String get() {
        return prefix + "_" + UUID.randomUUID();
    }
}
