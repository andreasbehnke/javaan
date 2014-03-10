---
layout: default
title: Javaan - Java Static Code Analyser
---

# Javaan
## A Java Static Code Analyser

Javaan is a command line tool for analysing java bytecode. It is not bound to the Java programming language 
but supports parsing of byte code from any java virtual machine based programming language. Javaan helps
fixing broken builds which produce archives with duplicate classes. It provides an overview about call graphs,
class dependency and class hierarchy.

Javaan uses the [apache BCEL library](http://commons.apache.org/proper/commons-bcel) for parsing java byte
code and the [graph library JGraphT](http://jgrapht.org/) for building the dependency graph and class graph.

### Authors

 * Andreas Behnke
 
### License

Javaan is licensed under the Apache License 2.0, view the LICENSE file for details
