package org.javaan.commands;

import org.apache.commons.cli.Option;

public interface StandardOptions {

	static final String OPT_IMPLEMENTATION = "impl";

	static final String OPT_SPECIALIZATIONS = "spec";

	static final String OPT_SUPER = "s";
	
	static final String OPT_INTERFACES = "i";
	
	static final String OPT_METHODS = "m";
	
	static final String OPT_VIRTUAL_METHODS = "vm";
	
	static final String OPT_FILTER = "f";
	
	static final String OPT_METHOD = "method";
	
	static final String OPT_LEAVES = "l";
	
	static final Option SUPER_TYPES = new Option(StandardOptions.OPT_SUPER, "super", false, "For each type, list the hierachy of super types");
	
	static final Option SPECIALIZATIONS = new Option(StandardOptions.OPT_SPECIALIZATIONS, "specializations", false, "For each type, list specialization types");
	
	static final Option INTERFACES = new Option(StandardOptions.OPT_INTERFACES, "interfaces", false, "For each class, list all implemented interfaces");
	
	static final Option METHODS = new Option(StandardOptions.OPT_METHODS, "methods", false, "For each type, list methods");
	
	static final Option VIRTUAL_METHODS = new Option(StandardOptions.OPT_VIRTUAL_METHODS, "virtualMethods", false, "For each type, list all methods and all inherited (virtual) methods");

	static final Option FILTER = new Option(StandardOptions.OPT_FILTER, "filter", true, "Filter types by <arg>. All types which contain <arg> in name will match.");

	static final Option METHOD = new Option(StandardOptions.OPT_METHOD, true, "Filter methods by <arg>. The format of a matching method is '<canonical class name> - <methodname><signature>'. "
			+ "All methods which contain <arg> in name will match.");

	static final Option LEAVES = new Option(StandardOptions.OPT_LEAVES, "leaves", false, "Only display leaf elements of graph.");
}
