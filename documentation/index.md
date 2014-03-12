---
layout: default
title:  "Documentation"
---

## Documentation

### Installation

 * download latest software release
 * unpack all files
 * make sure that javaan.sh or javaan.bat is in your system path
 * run "javaan --help" to test your installation

### Examples

List all classes of package org.javaan.commands:

	$ javaan classes target/javaan.jar -f org.javaan.commands 
	[C]org.javaan.commands.BaseCallGraphCommand 
	[C]org.javaan.commands.BaseCommand
	[C]org.javaan.commands.BaseDependencyGraphCommand 
	[C]org.javaan.commands.BaseGraphCommand
	[C]org.javaan.commands.BaseTypeLoadingCommand
	[C]org.javaan.commands.FilterUtil
	[C]org.javaan.commands.ListClasses
	[C]org.javaan.commands.ListDuplicates
	[C]org.javaan.commands.ListInterfaces
	[C]org.javaan.commands.MethodMatcher
	[C]org.javaan.commands.NameMatcher
	[C]org.javaan.commands.ShowCalleeGraph
	[C]org.javaan.commands.ShowCallerGraph
	[C]org.javaan.commands.ShowUsedGraph
	[C]org.javaan.commands.ShowUsingGraph
	[C]org.javaan.commands.SortUtil
	[C]org.javaan.commands.SortUtil$1

List all interfaces of package org.javaan.graph and their implementations:

	$ javaan interfaces target/javaan.jar -f org.javaan.graph -impl
	[I]org.javaan.graph.NamedObjectVisitor: [C]org.javaan.print.GraphPrinter

Display callgraphs of all methods of class ListInterfaces:

	$ javaan callees target/javaan.jar -method ListClasses | more
	[M]org.javaan.commands.ListClasses - public void <init>():
	[M]org.javaan.commands.ListClasses - public void <init>()
	 [M]org.javaan.commands.BaseCommand - public void <init>()
	
	--
	
	[M]org.javaan.commands.ListClasses - public org.apache.commons.cli.Options buildCommandLineOptions(org.apache.commons.cli.Options options):
	[M]org.javaan.commands.ListClasses - public org.apache.commons.cli.Options buildCommandLineOptions(org.apache.commons.cli.Options options)
	 [M]org.apache.commons.cli.Options - public org.apache.commons.cli.Options addOption(org.apache.commons.cli.Option opt)
	  [M]org.apache.commons.cli.Option - public boolean isRequired()
	  [M]org.apache.commons.cli.Option - public String getLongOpt()
	  [M]org.apache.commons.cli.Option - public boolean hasLongOpt()
	  
	[...]
	
Display leaf usage classes of class ListInterfaces:

	$ javaan using target/javaan.jar -f ListClasses --leaves
	[C]org.javaan.commands.ListClasses
		[C]org.javaan.JavaanCli

Display the class and interface dependency graph of package org.javaan.model
in 2D graph view:

	javaan used ~/src/javaan/javaan-lib/target/javaan-lib-2.0-SNAPSHOT.jar -f org.javaan.model -2d
	
The result looks like this:

<img src="/images/2d-call-graph-example.png" title="example package dependency graph" />

### Usage Reference

javaan is a tool for static code analysis. It is using byte code analysis to provide informations about the loaded
types. There are several sub commands for different tasks. The command name is followed by a list of jar, war or ear
files, which should be processed, and options.

Standard command line:

	$ javaan <command> <arguments> <options>

Display help:

	$ javaan --help

Display help of sub-command:
	
	$ javaan <command> --help

### Type Hierarchy Subcommands

#### packages       

List all packages of the libraries

options:
  
	-f,--filter <arg>   Filter types by <arg>. All types which contain <arg> in name will match.

#### classes

List all classes of the libraries

	-f,--filter <arg>         Filter types by <arg>. All types which contain <arg> in name will match.
	-i,--interfaces           For each class, list all implemented interfaces
	-m,--methods              For each type, list methods
	-s,--super                For each type, list the hierachy of super types
	-spec,--specializations   For each type, list specialization types
	-vm,--virtual-methods     For each type, list all methods and all inherited (virtual) methods

#### interfaces

List all interfaces of the libraries

	-f,--filter <arg>         Filter types by <arg>. All types which contain <arg> in name will match.
	-impl,--implementations   For each interfaces, list all implementing classes.
	-m,--methods              For each type, list methods
	-s,--super                For each type, list the hierachy of super types
	-spec,--specializations   For each type, list specialization types
	-vm,--virtual-methods     For each type, list all methods and all inherited (virtual) methods

#### duplicates

List all duplicate classes and interfaces of the libraries being loaded. Classes are marked as
duplications if they share canonical name, they may vary in bytecode and file location.

#### missing-types

List types which are referenced by loaded types but could not be resolved.

### Dependency Graph Subcommands

#### cycles

Show call graph for each dependency cycle in the loaded libraries. Cycles within class hierachies are omitted,
if option -rdh is not provided.

	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### callers

Display the graph of methods which call another method. This is the bottom up view of the call graph.

	-l,--leaves                                      Only display leaf elements of graph.
	-method <arg>                                    Filter methods by <arg>. The format of a matching method is
		                                              '<canonical class name> - <methodname><signature>'. All methods which
		                                              contain <arg> in name will match.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### callees

Display the graph of methods being called by another method. This is the top down view of the call graph.

	-l,--leaves                                      Only display leaf elements of graph.
	-method <arg>                                    Filter methods by <arg>. The format of a matching method is
		                                              '<canonical class name> - <methodname><signature>'. All methods which
		                                              contain <arg> in name will match.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### used

Display the graph of classes being used by another class. This is the top down view of the class dependency graph.

	-2d,--display-2d-graph                           Displays result in a 2D graph representation.
	-f,--filter <arg>                                Filter types by <arg>. All types which contain <arg> in name will
		                                              match.
	-l,--leaves                                      Only display leaf elements of graph.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### using

Display the graph of classes using another class. This is the bottom up view of the class dependency graph.

	-2d,--display-2d-graph                           Displays result in a 2D graph representation.
	-f,--filter <arg>                                Filter types by <arg>. All types which contain <arg> in name will
		                                              match.
	-l,--leaves                                      Only display leaf elements of graph.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### package-cycles

Show call graph for each package dependency cycle in the loaded libraries.

	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### used-packages

Display the graph of packages being used by another package. This is the top down view of the package dependency graph.

	-2d,--display-2d-graph                           Displays result in a 2D graph representation.
	-f,--filter <arg>                                Filter types by <arg>. All types which contain <arg> in name will
		                                              match.
	-l,--leaves                                      Only display leaf elements of graph.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

#### using-packages

Display the graph of packages using another package. This is the bottom up view of the package dependency graph.

	-2d,--display-2d-graph                           Displays result in a 2D graph representation.
	-f,--filter <arg>                                Filter types by <arg>. All types which contain <arg> in name will
		                                              match.
	-l,--leaves                                      Only display leaf elements of graph.
	-rdh,--resolve-dependencies-in-class-hierarchy   Enables resolve of dependencies within class hiearchy.
	-rmi,--resolve-method-implementations            Enables resolve of method implementations for abstract methods.

### Configuration Subcommands

#### set

Makes options persistent across consecutive calls. If no option is provided, lists all persistent
options. The following options can be set, separated by whitespace: 

 * rmi (resolve-method-implementations) 
 * rdh (resolve-dependencies-in-class-hierarchy) 
 * 2d (display-2d-graph) 

#### reset

Resets options which have been made persistent with set command. If no option is provided, lists all
persistent options. The following options can be resetted, separated by whitespace: 

 * rmi (resolve-method-implementations) 
 * rdh (resolve-dependencies-in-class-hierarchy) 
 * 2d (display-2d-graph) 

