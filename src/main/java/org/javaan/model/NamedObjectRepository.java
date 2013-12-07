package org.javaan.model;

public interface NamedObjectRepository<N extends NamedObject> {

	N get(String name);
}
