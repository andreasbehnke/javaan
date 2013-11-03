package org.javaan;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;

public interface TestConstants {

	public static final String TEST_JAR_FILE = "src/test/resources/testJar.jar";
	
	public static final int NUMBER_OF_CLASSES = 3;
	
	public static final int NUMBER_OF_INTERFACES = 4;
	
	public static final int NUMBER_OF_CLASSES_AND_INTERFACES = NUMBER_OF_CLASSES + NUMBER_OF_INTERFACES;
	
	public static final Clazz CLASS_A = new Clazz("org.javaan.test.jar.ClassA");
	
	public static final Clazz CLASS_C = new Clazz("org.javaan.test.jar.ClassC");
	
	public static final Clazz SUPER_CLASS_OF_CLASS_C = new Clazz("org.javaan.test.jar.ClassB");

	public static final Interface INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceC");
	
	public static final Interface SUPER_INTERFACE1_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceB");
	
	public static final Interface SUPER_INTERFACE2_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceA");
}