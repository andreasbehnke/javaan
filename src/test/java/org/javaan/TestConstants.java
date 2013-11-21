package org.javaan;

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Method;

/**
 * Expected values when using the testJar.jar in integration tests.
 */
public interface TestConstants {

	public static final String TEST_JAR_FILE = "src/test/resources/testJar.jar";
	
	public static final int NUMBER_OF_CLASSES = 6;
	
	public static final int NUMBER_OF_INTERFACES = 4;
	
	public static final int NUMBER_OF_CLASSES_AND_INTERFACES = NUMBER_OF_CLASSES + NUMBER_OF_INTERFACES;

	
	public static final Clazz CLASS_A = new Clazz("org.javaan.test.jar.ClassA");
	
	public static final Clazz CLASS_B = new Clazz("org.javaan.test.jar.ClassB");

	public static final Clazz CLASS_C = new Clazz("org.javaan.test.jar.ClassC");

	public static final Clazz SUPER_CLASS_OF_CLASS_C = new Clazz("org.javaan.test.jar.ClassB");
	
	public static final Clazz ABSTACT_CLASS_A = new Clazz("org.javaan.test.jar.AbstractClassA");

	public static final Clazz SPECIALIZATION_CLASS_B = new Clazz("org.javaan.test.jar.SpecializationClassB");

	public static final Clazz CLASS_CALLING_ABSTACT_METHOD = new Clazz("org.javaan.test.jar.ClassCallingAbstractMethod");

	public static final Interface INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceC");
	
	public static final Interface INTERFACE_B = new Interface("org.javaan.test.jar.InterfaceB");
	
	public static final Interface SUPER_INTERFACE1_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceB");
	
	public static final Interface SUPER_INTERFACE2_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceA");

	
	public static final String SIGNATURE_CONSTRUCTOR = "<init>()V";
	
	public static final String SIGNATURE_METHOD_INTERFACE_B = "methodInterfaceB(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
	
	public static final String SIGNATURE_METHOD_CLASS_B = "methodClassB(Lorg/javaan/test/jar/InterfaceC;)V";
	
	public static final String SIGNATURE_ENTRY_METHOD = "entryMethod(Lorg/javaan/test/jar/InterfaceB;)V";
	
	public static final String SIGNATURE_ABSTACT_METHOD = "abstractMethod()V";
	
	public static final String SIGNATURE_CALL_ABSTRACT_METHID = "callAbstractMethod(Lorg/javaan/test/jar/AbstractClassA;)V";

	
	public static final Method CLASSA_METHOD_INTERFACE_B = new Method(CLASS_A, null, SIGNATURE_METHOD_INTERFACE_B);
	
	public static final Method CLASSB_METHOD_CLASS_B = new Method(CLASS_B, null, SIGNATURE_METHOD_CLASS_B);
	
	public static final Method CLASSC_ENTRY_METHOD = new Method(CLASS_C, null, SIGNATURE_ENTRY_METHOD);
	
	public static final Method CLASSB_CONSTRUCTOR = new Method(CLASS_B, null, SIGNATURE_CONSTRUCTOR);
	
	public static final Method ABSTRACT_CLASS_A_ABSTRACT_METHOD = new Method(ABSTACT_CLASS_A, null, SIGNATURE_ABSTACT_METHOD);

	public static final Method SPECIALIZATION_CLASS_B_ABSTRACT_METHOD = new Method(SPECIALIZATION_CLASS_B, null, SIGNATURE_ABSTACT_METHOD);

	public static final Method CLASS_CALLING_ABSTACT_METHOD_CALL_ABSTRACT_METHOD = new Method(CLASS_CALLING_ABSTACT_METHOD, null, SIGNATURE_CALL_ABSTRACT_METHID);
}