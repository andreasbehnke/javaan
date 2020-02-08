package org.javaan.bytecode;

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

import org.apache.bcel.classfile.Method;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.javaan.graph.GraphFactory;
import org.javaan.graph.ParentChildMap;
import org.javaan.graph.RandomEdgeSupplier;
import org.javaan.graph.Tree;
import org.javaan.model.*;
import org.javaan.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Creates class context from bytecode, references to classes outside the loaded libraries are resolved
 * using {@link ReflectionClassContextBuilder}.
 */
public class ClassContextBuilder {
	
	private final static Logger LOG = LoggerFactory.getLogger(ClassContextBuilder.class);

	private Set<String> missingTypes;

    private NamedObjectMap<Type> typeLookup;

    private List<org.javaan.model.Method> getMethodsOfType(Type type) {
	    List<org.javaan.model.Method> methods = new ArrayList<>();
	    if (type.isReflection()) {
            for (java.lang.reflect.Method method : type.getReflectionClass().getDeclaredMethods()) {
                methods.add(org.javaan.model.Method.create(type, method));
            }
            for (Constructor<?> constructor : type.getReflectionClass().getConstructors()) {
                methods.add(org.javaan.model.Method.create(type, constructor));
            }
        } else {
            for (Method method : type.getJavaClass().getMethods()) {
                methods.add(org.javaan.model.Method.create(type, method));
            }
        }
        return methods;
    }

    private Interface getInterface(String interfaceName) {
        Type interfaze = typeLookup.get(interfaceName);
        if (interfaze == null) {
            LOG.warn("Could not find interface " + interfaceName);
            return null;
        }
        return interfaze.toInterface();
    }

    private List<Interface> getInterfacesOfClass(Clazz clazz) {
	    return clazz.getInterfaceNames().stream()
                .map(this::getInterface)
                .collect(Collectors.toList());
    }

    private List<Interface> getSuperInterfacesOfInterface(Interface interfaze) {
	    return interfaze.getSuperInterfaceNames().stream()
                .map(this::getInterface)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void addSuperClass(Clazz clazz, Clazz superClazz, Tree<Clazz, String> superClassTree) {
        if (superClazz == null) {
            superClassTree.addVertex(clazz);
        } else {
            superClassTree.addEdge(superClazz, clazz);
        }
    }

    private Clazz getSuperClazz(Clazz clazz) {
        String superType = clazz.getSuperTypeName();
        Type type = typeLookup.get(superType);
        if (type == null) {
            LOG.warn("Could not find class " + superType);
            return null;
        } else {
            return type.toClazz();
        }
    }

	public ClassContext build(List<Type> types) {
        LOG.info("Creating class context ...");
		Date start = new Date();

		// ---- load missing types ----

        ReflectionTypeLoader loader = new ReflectionTypeLoader();
		types = loader.loadMissingTypes(types);
        typeLookup = new NamedObjectMap<>(types);
        missingTypes = loader.getMissingTypes();

		// ---- process packages ----

        ClassContextInternals internals = new ClassContextInternals();
        internals.types = typeLookup;
        
        // build types of package
        internals.typesOfPackage = new ParentChildMap<>(
                types.stream()
                        .collect(Collectors.groupingBy(Package::new)));

        // ---- process classes ----

		// build super class hierarchy
        List<Clazz> classes = types.parallelStream()
                .filter(type -> type.getJavaType() == Type.JavaType.CLASS)
                .map(Type::toClazz)
                .collect(Collectors.toList());
		internals.superClass = GraphFactory.createVertexEdgeTree(new RandomEdgeSupplier("superClassOf"));
        classes.parallelStream()
                .map(clazz -> new ImmutablePair<>(clazz, getSuperClazz(clazz)))
                .collect(Collectors.toList()).stream() // interrupt parallel processing because graph library does not support multithreading
                .forEach(clazzSuperClazz -> addSuperClass(clazzSuperClazz.getLeft(), clazzSuperClazz.getRight(), internals.superClass));

		// build interfaces of class
        internals.interfacesOfClass = new ParentChildMap<>(
        classes.stream()
                .collect(Collectors.toMap(Function.identity(), this::getInterfacesOfClass))
        );

        // build implementation of interface
        internals.implementationOfInterface = internals.interfacesOfClass.invers();

        // ---- process interfaces ----
        List<Interface> interfaces = types.parallelStream()
                .filter(type -> type.getJavaType() == Type.JavaType.INTERFACE)
                .map(Type::toInterface)
                .collect(Collectors.toList());
        internals.superInterface = GraphFactory.createVertexEdgeGraph(new RandomEdgeSupplier("superInterfaceOf"));
        interfaces.parallelStream()
                .map(anInterface -> new ImmutablePair<>(anInterface, getSuperInterfacesOfInterface(anInterface)))
                .collect(Collectors.toList()).stream() // interrupt parallel processing
                .filter(interfaceListImmutablePair -> interfaceListImmutablePair.getLeft() != null)
                .forEach(interfaceSuperInterfaces -> internals.superInterface.addEdges(interfaceSuperInterfaces.getLeft(), interfaceSuperInterfaces.getRight()));

        // ---- process methods
        internals.methodsOfClass = new ParentChildMap<>(
                classes.stream()
                .collect(Collectors.toMap(Function.identity(), this::getMethodsOfType))
        );

        internals.methodsOfInterface = new ParentChildMap<>(
                interfaces.stream()
                .collect(Collectors.toMap(Function.identity(), this::getMethodsOfType))
        );

        ClassContext context = new ClassContext(internals);
		long duration = new Date().getTime() - start.getTime();
        LOG.info("Creation of class context with {} classes and {} interfaces took {} ms",
				context.getClasses().size(), context.getInterfaces().size(), duration);
		int numberOfMissingTypes = missingTypes.size();
		if (numberOfMissingTypes > 0) {
			LOG.warn("Missing types: {} types could not be resoled", numberOfMissingTypes);
		}
		return context;
	}
	
	public Set<String> getMissingTypes() {
		return missingTypes;
	}
}
