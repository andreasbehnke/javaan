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
	
	static final String OPT_CALLERS = "callers";
	
	static final Option SUPER_TYPES = new Option(StandardOptions.OPT_SUPER, "superTypes", false, "For each type list the hierachy of super types");
	
	static final Option SPECIALIZATIONS = new Option(StandardOptions.OPT_SPECIALIZATIONS, "specializations", false, "For each type list specialization types");
	
	static final Option INTERFACES = new Option(StandardOptions.OPT_INTERFACES, "interfaces", false, "For each class list all implemented interfaces");
	
	static final Option METHODS = new Option(StandardOptions.OPT_METHODS, "methods", false, "For each type list methods");
	
	static final Option VIRTUAL_METHODS = new Option(StandardOptions.OPT_VIRTUAL_METHODS, "virtualMethods", false, "For each type list all methods and all inherited (virtual) methods");

	static final Option FILTER = new Option(StandardOptions.OPT_FILTER, "filter", true, "Filter types by <arg>. All types which contain <arg> in name will match.");

	static final Option CALLERS = new Option(OPT_CALLERS, "Instead of traversing the call graph from caller to callee, list all callers of the given methods.");
}
