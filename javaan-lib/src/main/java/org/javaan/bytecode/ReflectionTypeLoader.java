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

    private List<String> resolveDependencies(Type type, Set<String> typeLookup) {
        List<String> newTypes = new ArrayList<>();
        switch (type.getJavaType()) {
            case CLASS:
                Clazz clazz = type.toClazz();
                if (clazz.getSuperTypeName() != null) {
                    newTypes.add(clazz.getSuperTypeName());
                }
                if (clazz.getInterfaceNames() != null) {
                    newTypes.addAll(clazz.getInterfaceNames());
                }
                break;
            case INTERFACE:
                Interface interfaze = type.toInterface();
                if(interfaze.getSuperInterfaceNames() != null) {
                    newTypes.addAll(interfaze.getSuperInterfaceNames());
                }
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
            typeLookup.addAll(createTypeLookup(typesToResolve));
        } while ((typesToResolve.size() > 0));
        return loadedTypes;
    }


}
