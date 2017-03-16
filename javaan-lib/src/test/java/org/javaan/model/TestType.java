package org.javaan.model;

import org.javaan.bytecode.JarFileLoader;
import org.javaan.bytecode.TestConstants;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestType implements TestConstants {

    @Test
    public void testCreateClassFromReflection() throws ClassNotFoundException {

        // public class ArrayList<E> extends AbstractList<E>
        // implements List<E>, RandomAccess, Cloneable, java.io.Serializable

        Clazz clazz = Type.create(ArrayList.class.getName()).toClazz();
        assertEquals("java.util.AbstractList", clazz.getSuperTypeName());
        assertEquals(4, clazz.getInterfaceNames().size());
        assertTrue(clazz.getInterfaceNames().contains("java.util.List"));
        assertTrue(clazz.getInterfaceNames().contains("java.util.RandomAccess"));
        assertTrue(clazz.getInterfaceNames().contains("java.lang.Cloneable"));
        assertTrue(clazz.getInterfaceNames().contains("java.io.Serializable"));
    }

    @Test
    public void testCreateInterfaceFromReflection() throws ClassNotFoundException {

        // public interface List<E> extends Collection<E>

        Interface interfaze = Type.create(List.class.getName()).toInterface();
        assertEquals(1, interfaze.getSuperInterfaceNames().size());
        assertEquals("java.util.Collection", interfaze.getSuperInterfaceNames().get(0));
    }

    private List<Type> loadClasses() throws IOException {
        return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
    }

    private Type findType(List<Type> types, String typeName) {
        for (Type type: types) {
            if (type.getName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    @Test
    public void testCreateClassFromBytecode() throws IOException {
        List<Type> types = loadClasses();
        Clazz class_c = findType(types, CLASS_C.getName()).toClazz();
        assertEquals(SUPER_CLASS_OF_CLASS_C.getName(), class_c.getSuperTypeName());
        Clazz class_a = findType(types, CLASS_A.getName()).toClazz();
        assertEquals(1, class_a.getInterfaceNames().size());
        assertEquals(INTERFACE_C.getName(), class_a.getInterfaceNames().get(0));
    }

    @Test
    public void testCreateInterfaceFromBytecode() throws IOException {
        List<Type> types = loadClasses();
        Interface interface_c = findType(types, INTERFACE_C.getName()).toInterface();
        assertEquals(2, interface_c.getSuperInterfaceNames().size());
        assertTrue(interface_c.getSuperInterfaceNames().contains(SUPER_INTERFACE1_OF_INTERFACE_C.getName()));
        assertTrue(interface_c.getSuperInterfaceNames().contains(SUPER_INTERFACE2_OF_INTERFACE_C.getName()));
    }
}
