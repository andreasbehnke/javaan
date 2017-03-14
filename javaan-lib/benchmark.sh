#!/bin/bash
mvn install
mvn exec:java -Dexec.mainClass="org.javaan.bytecode.BenchmarkJarFileLoader" -Dexec.args="/home/andreasbehnke/.m2"