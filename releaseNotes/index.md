---
layout: default
title:  "Release Notes"
---

## Release Notes

### Release 2.1

#### Java 8

Updated bcel library to support java 8 bytecode.

### Release 2.0

#### 2D Display for Dependency Graphs

Graph based results can be displayed in 2D GUI window using the -2d option.
This option is supported for the sub-commands used, using, used-packages and
using-packages.

#### Package Dependency Graph

The three sub-commands used-packages, using-packages and package-cycle provide
methods for analysing dependencies between packages.

#### Dependency Graph - Implementation resolval is optional

Made the resolval of implementations of abstract methods (interface and abstract methods)
optional. Resolval of implementations blow up dependency graph and make results hard
to understand. Use option -rmi,--resolve-method-implementations to enable this feature again.

#### Persistent Configuration
Several options can be made persistent to be used in consecutive command calls.
The sub-command "set" make options persistent, the sub-command "reset" clears
persistent options.

### Release 1.0

initial release
