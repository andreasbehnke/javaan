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

	String OPT_IMPLEMENTATIONS = "impl";

	String OPT_SPECIALIZATIONS = "spec";

	String OPT_SUPER = "s";

	String OPT_INTERFACES = "i";

	String OPT_METHODS = "m";

	String OPT_VIRTUAL_METHODS = "vm";

	String OPT_FILTER = "f";

	String OPT_METHOD = "method";

	String OPT_LEAVES = "l";

	String OPT_TOPOLOGICAL_SORT = "topo";

	String OPT_RESOLVE_METHOD_IMPLEMENTATIONS = "rmi";

	String OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY = "rdh";

	String OPT_DISPLAY_2D_GRAPH = "2d";

	Option SUPER_TYPES = new Option(OPT_SUPER, "super", false, "For each type, list the hierarchy of super types");

	Option SPECIALIZATIONS = new Option(OPT_SPECIALIZATIONS, "specializations", false, "For each type, list specialization types");

	Option INTERFACES = new Option(OPT_INTERFACES, "interfaces", false, "For each class, list all implemented interfaces");

	Option IMPLEMENTATIONS = new Option(OPT_IMPLEMENTATIONS, "implementations", false, "For each interfaces, list all implementing classes.");

	Option METHODS = new Option(OPT_METHODS, "methods", false, "For each type, list methods");

	Option VIRTUAL_METHODS = new Option(OPT_VIRTUAL_METHODS, "virtual-methods", false, "For each type, list all methods and all inherited (virtual) methods");

	Option FILTER = new Option(OPT_FILTER, "filter", true, "Filter types by <arg>. All types which contain <arg> in name will match.");

	Option METHOD = new Option(OPT_METHOD, true, "Filter methods by <arg>. The format of a matching method is '<canonical class name> - <methodname><signature>'. "
			+ "All methods which contain <arg> in name will match. "
			+ "If <arg> starts with \"a:<subarg>\", e.g. \"a:Schedule\", all methods with an annotation which contain <subarg> in name will match.");

	Option LEAVES = new Option(OPT_LEAVES, "leaves", false, "Only display leaf elements of graph.");

	Option TOPOLOGICAL_SORT = new Option(OPT_TOPOLOGICAL_SORT, "topological-sort", false, "Sort dependency graph in topological order. Starts with types having no dependencies.");

	Option RESOLVE_METHOD_IMPLEMENTATIONS = new Option(OPT_RESOLVE_METHOD_IMPLEMENTATIONS, "resolve-method-implementations", false, "Enables resolve of method implementations for abstract methods.");

	Option RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY = new Option(OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY, "resolve-dependencies-in-class-hierarchy", false, "Enables resolve of dependencies within class hierarchy.");

	Option DISPLAY_2D_GRAPH = new Option(OPT_DISPLAY_2D_GRAPH, "display-2d-graph", false, "Displays result in a 2D graph representation.");

	Option[] PERSISTENT_OPTIONS = new Option[]{
		RESOLVE_METHOD_IMPLEMENTATIONS,
		RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY,
		DISPLAY_2D_GRAPH
	};
}
