package org.javaan.bytecode;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Type;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Loads missing types using java reflection mechanism
 */
public class ReflectionTypeLoader {

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    private Set<String> missingTypes = Collections.synchronizedSet(new HashSet<>());

    public Set<String> getMissingTypes() {
        return missingTypes;
    }

    private Type loadType(String name) {
        try {
            return Type.create(name);
        } catch (ClassNotFoundException e) {
            missingTypes.add(name);
            return null;
        }
    }

    private void addTypesNeedLoading(List<String> newTypes, Set<String> typeLookup, String typeName) {
        if (typeName != null && !typeLookup.contains(typeName)) {
            newTypes.add(typeName);
        }
    }

    private void addTypesNeedLoading(List<String> newTypes, Set<String> typeLookup, List<String> typeNames) {
        if (typeNames == null) return;
        for (String typeName: typeNames) {
            addTypesNeedLoading(newTypes, typeLookup, typeName);
        }
    }

    private List<String> resolveDependencies(Type type, Set<String> typeLookup) {
        List<String> newTypes = new ArrayList<>();
        switch (type.getJavaType()) {
            case CLASS:
                Clazz clazz = type.toClazz();
                String superClassName = (clazz.getSuperTypeName() == null) ? JAVA_LANG_OBJECT : clazz.getSuperTypeName();
                addTypesNeedLoading(newTypes, typeLookup, superClassName);
                addTypesNeedLoading(newTypes, typeLookup, clazz.getInterfaceNames());
                break;
            case INTERFACE:
                Interface interfaze = type.toInterface();
                addTypesNeedLoading(newTypes, typeLookup, interfaze.getSuperInterfaceNames());
                break;
        }
        return newTypes;
    }

    private Set<String> createTypeLookup(List<Type> types) {
        return types.stream()
                .map(type -> type.getName())
                .collect(Collectors.toSet());
    }

    public List<Type> loadMissingTypes(List<Type> loadedTypes) {
        Set<String> typeLookup = createTypeLookup(loadedTypes);
        List<Type> typesToResolve = new ArrayList<>(loadedTypes);
        do {
            Set<String> newTypeNames = typesToResolve.stream()
                    .map(type -> resolveDependencies(type, typeLookup))
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());
            typesToResolve = newTypeNames.parallelStream()
                    .map(typeName -> loadType(typeName))
                    .filter(type -> type != null)
                    .collect(Collectors.toList());
            loadedTypes.addAll(typesToResolve);
            typeLookup.addAll(newTypeNames);
        } while ((typesToResolve.size() > 0));
        return loadedTypes;
    }


}
