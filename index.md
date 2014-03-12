---
layout: default
title: Java Static Code Analyser
---

# Javaan
## A Java Static Code Analyser

Javaan is a command line tool for analysing java bytecode. It is not bound to the Java programming language 
but supports parsing of any JVM byte code. Javaan helps fixing broken builds which produce archives with duplicate classes. 
It provides an overview about call graphs, class dependency and class hierarchy. This helps analysing unknown and legacy code. 
You get an impression about the code quality of black box projects.

Javaan uses the [apache BCEL library](http://commons.apache.org/proper/commons-bcel) for parsing java byte
code and the [graph library JGraphT](http://jgrapht.org/) for building the dependency graph and class graph.

<img src="/images/2d-graph-example.png" title="example package dependency graph" class="right" />

#### 2D Graph Display

The latest version supports display of dependency graphs in a 2D graph display. This is more comfortable
than reading a graph dump at console. The thickness of the Graph edges represent the number of method
calls between the nodes.

The diagram at the right side shows the packages of the javaan core library. It is the result of the
command 

<code>javaan used-packages javaan-lib-2.0-SNAPSHOT.jar -f org.javaan</code>

Notice the dependency cycle between packages org.javaan.model and org.javaan.bytecode. This cycle is 
not ideal and might be a candidate for refactoring. Perhaps the 7 method calls 
from org.javaan.model to org.javaan.bytecode should be eliminated to get a directed acyclic dependency graph.

#### Authors

 * Andreas Behnke
 
#### License

Javaan is licensed under the Apache License 2.0, view the LICENSE file for details

<p style="clear:both" />
