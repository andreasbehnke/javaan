package org.javaan.commands;

/*
 * #%L
 * Java Static Code Analysis
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.cli.Option;

public interface StandardOptions {

	static final String OPT_IMPLEMENTATIONS = "impl";

	static final String OPT_SPECIALIZATIONS = "spec";

	static final String OPT_SUPER = "s";
	
	static final String OPT_INTERFACES = "i";
	
	static final String OPT_METHODS = "m";
	
	static final String OPT_VIRTUAL_METHODS = "vm";
	
	static final String OPT_FILTER = "f";
	
	static final String OPT_METHOD = "method";
	
	static final String OPT_LEAVES = "l";

	static final String OPT_TOPOLOGICAL_SORT = "topo";
	
	static final String OPT_RESOLVE_METHOD_IMPLEMENTATIONS = "rmi";
	
	static final String OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY = "rdh";
	
	static final String OPT_DISPLAY_2D_GRAPH = "2d";
	
	static final Option SUPER_TYPES = new Option(OPT_SUPER, "super", false, "For each type, list the hierachy of super types");
	
	static final Option SPECIALIZATIONS = new Option(OPT_SPECIALIZATIONS, "specializations", false, "For each type, list specialization types");
	
	static final Option INTERFACES = new Option(OPT_INTERFACES, "interfaces", false, "For each class, list all implemented interfaces");
	
	static final Option IMPLEMENTATIONS = new Option(OPT_IMPLEMENTATIONS, "implementations", false, "For each interfaces, list all implementing classes.");
	
	static final Option METHODS = new Option(OPT_METHODS, "methods", false, "For each type, list methods");
	
	static final Option VIRTUAL_METHODS = new Option(OPT_VIRTUAL_METHODS, "virtual-methods", false, "For each type, list all methods and all inherited (virtual) methods");

	static final Option FILTER = new Option(OPT_FILTER, "filter", true, "Filter types by <arg>. All types which contain <arg> in name will match.");

	static final Option METHOD = new Option(OPT_METHOD, true, "Filter methods by <arg>. The format of a matching method is '<canonical class name> - <methodname><signature>'. "
			+ "All methods which contain <arg> in name will match. "
			+ "If <arg> starts with \"a:<subarg>\", e.g. \"a:Schedule\", all methods with an annotation which contain <subarg> in name will match.");

	static final Option LEAVES = new Option(OPT_LEAVES, "leaves", false, "Only display leaf elements of graph.");

	static final Option TOPOLOGICAL_SORT = new Option(OPT_TOPOLOGICAL_SORT, "topological-sort", false, "Sort dependency graph in topological order. Starts with types having no dependencies.");

	static final Option RESOLVE_METHOD_IMPLEMENTATIONS = new Option(OPT_RESOLVE_METHOD_IMPLEMENTATIONS, "resolve-method-implementations", false, "Enables resolve of method implementations for abstract methods.");
	
	static final Option RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY = new Option(OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY, "resolve-dependencies-in-class-hierarchy", false, "Enables resolve of dependencies within class hiearchy.");

	static final Option DISPLAY_2D_GRAPH = new Option(OPT_DISPLAY_2D_GRAPH, "display-2d-graph", false, "Displays result in a 2D graph representation.");

	static final Option[] PERSISTENT_OPTIONS = new Option[]{
		RESOLVE_METHOD_IMPLEMENTATIONS,
		RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY,
		DISPLAY_2D_GRAPH		
	};
}
