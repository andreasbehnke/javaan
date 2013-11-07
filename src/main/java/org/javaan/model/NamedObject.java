package org.javaan.model;

/**
 * An object which is sortable and equals by it's name
 */
public interface NamedObject extends Comparable<NamedObject> {

	String getName();
}
