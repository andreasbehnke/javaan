# Javaan
## A Java Static Code Analyser

Javaan is a command line tool for analysing java bytecode. It is not bound to the Java programming language 
but supports parsing of byte code from any java virtual machine based programming language. Javaan helps
fixing broken builds which produce archives with duplicate classes. It provides an overview about call graphs,
class dependency and class hierarchy.

Javaan uses the [apache BCEL library](http://commons.apache.org/proper/commons-bcel) for parsing java byte
code and the [graph library JGraphT](http://jgrapht.org/) for 

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

For a complete command reference help type:

	$ javaan --help
	
### Authors

 * Andreas Behnke
 
### Licence

Javaan is licensed under the Apache License 2.0, view the LICENSE file for details