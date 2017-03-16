package org.javaan.bytecode;

import static org.junit.Assert.*;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Type;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestReflectionTypeLoader implements TestConstants {

    public static final Clazz CLASS_WITH_UNKNOWN_SUPER_CLASS = new Clazz("org.ClassWithUnknownSuperClass", "nobody.knows.this", null);

    public static final Clazz CLASS_WITH_UNKNOWN_INTERFACES = new Clazz("org.ClassWithUnknownInterfaces", null, Arrays.asList("bar", "foo"));

    public static final Clazz CLASS_JAVA_LANG_OBJECT = new Clazz("java.lang.Object");

    public static final Clazz CLASS_JAVA_LANG_NUMBER = new Clazz("java.lang.Number");

    public static final Interface INTERFACE_JAVA_LANG_COMPARABLE = new Interface("java.lang.Comparable");

    public static final Interface INTERFACE_JAVA_IO_SERIALIZABLE = new Interface("java.io.Serializable");

    public static final Clazz CLASS_JAVA_LANG_INTEGER = new Clazz("java.lang.Integer");

    public static final Clazz CLASS_WITH_SUPER_TYPE = new Clazz("org.classwithsupertype", CLASS_JAVA_LANG_INTEGER.getName(), null);

    public static final Clazz CLASS_WITH_INTERFACES = new Clazz("org.ClassWithInterfaces", null, Arrays.asList("java.lang.Comparable", "java.io.Serializable"));

    @Test
    public void testNoSuperClassNorInterfaceToLoad() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_A);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(2, types.size());
        assertTrue(types.contains(CLASS_A));
        assertTrue(types.contains(CLASS_JAVA_LANG_OBJECT));
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
    public void testLoadSuperClassObject() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_A);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(2, types.size());
        assertTrue(types.contains(CLASS_A));
        assertTrue(types.contains(CLASS_JAVA_LANG_OBJECT));
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

    @Test
    public void testLoadInterfaces() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_WITH_INTERFACES);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(4, types.size());
        assertTrue(types.contains(CLASS_WITH_INTERFACES));
        assertTrue(types.contains(CLASS_JAVA_LANG_OBJECT));
        assertTrue(types.contains(INTERFACE_JAVA_LANG_COMPARABLE));
        assertTrue(types.contains(INTERFACE_JAVA_IO_SERIALIZABLE));
        assertEquals(0, reflectionTypeLoader.getMissingTypes().size());
    }

    @Test
    public void testLoadUnknownInterfaces() {
        List<Type> types = new ArrayList<>();
        types.add(CLASS_WITH_UNKNOWN_INTERFACES);
        ReflectionTypeLoader reflectionTypeLoader = new ReflectionTypeLoader();
        types = reflectionTypeLoader.loadMissingTypes(types);
        assertEquals(2, types.size());
        assertEquals(2, reflectionTypeLoader.getMissingTypes().size());
        assertTrue(reflectionTypeLoader.getMissingTypes().contains("foo"));
        assertTrue(reflectionTypeLoader.getMissingTypes().contains("bar"));
    }
}
