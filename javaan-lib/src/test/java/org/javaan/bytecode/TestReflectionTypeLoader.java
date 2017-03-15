package org.javaan.bytecode;

import static org.junit.Assert.*;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Type;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestReflectionTypeLoader implements TestConstants {

    public static final Clazz CLASS_WITH_UNKNOWN_SUPER_CLASS = new Clazz("org.ClassWithUnknownSuperClass", "nobody.knows.this", null);

    public static final Clazz CLASS_JAVA_LANG_OBJECT = new Clazz("java.lang.Object");

    public static final Clazz CLASS_JAVA_LANG_NUMBER = new Clazz("java.lang.Number");

    public static final Interface INTERFACE_JAVA_LANG_COMPARABLE = new Interface("java.lang.Comparable");

    public static final Interface INTERFACE_JAVA_IO_SERIALIZABLE = new Interface("java.io.Serializable");

    public static final Clazz CLASS_JAVA_LANG_INTEGER = new Clazz("java.lang.Integer");

    public static final Clazz CLASS_WITH_SUPER_TYPE = new Clazz("org.classwithsupertype", CLASS_JAVA_LANG_INTEGER.getName(), null);

    @Test
    public void testNoTypesToLoad() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_A);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(1, types.size());
        assertEquals(CLASS_A, types.get(0));
        assertEquals(0, reflectionTypeLoader.getMissingTypes().size());
    }

    @Test
    public void testLoadSuperClass() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_WITH_SUPER_TYPE);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(6, types.size());
        assertTrue(types.contains(CLASS_WITH_SUPER_TYPE));
        assertTrue(types.contains(CLASS_JAVA_LANG_INTEGER));
        assertTrue(types.contains(CLASS_JAVA_LANG_NUMBER));
        assertTrue(types.contains(CLASS_JAVA_LANG_OBJECT));
        assertTrue(types.contains(INTERFACE_JAVA_LANG_COMPARABLE));
        assertTrue(types.contains(INTERFACE_JAVA_IO_SERIALIZABLE));
        assertEquals(0, reflectionTypeLoader.getMissingTypes().size());
    }

    @Test
    public void testLoadUnknownSuperClass() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_WITH_UNKNOWN_SUPER_CLASS);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(1, types.size());
        assertTrue(types.contains(CLASS_WITH_UNKNOWN_SUPER_CLASS));
        assertEquals(1, reflectionTypeLoader.getMissingTypes().size());
        assertTrue(reflectionTypeLoader.getMissingTypes().contains("nobody.knows.this"));
    }
}
