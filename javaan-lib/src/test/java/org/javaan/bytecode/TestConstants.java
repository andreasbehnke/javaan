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

import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.Method;

/**
 * Expected values when using the testJar.jar in integration tests.
 */
public interface TestConstants {

	public static final String TEST_JAR_FILE = "src/test/resources/testJar.jar";
	
	public static final int NUMBER_OF_CLASSES = 7;

	public static final int NUMBER_OF_EXTERNAL_CLASSES = 2;

	public static final int NUMBER_OF_INTERFACES = 4;
	
	public static final int NUMBER_OF_EXTERNAL_INTERFACES = 2;
	
	public static final int NUMBER_OF_CLASSES_AND_INTERFACES = NUMBER_OF_CLASSES + NUMBER_OF_INTERFACES;

	
	public static final Clazz CLASS_A = new Clazz("org.javaan.test.jar.ClassA");
	
	public static final Clazz CLASS_B = new Clazz("org.javaan.test.jar.ClassB");

	public static final Clazz CLASS_C = new Clazz("org.javaan.test.jar.ClassC");

	public static final Clazz SUPER_CLASS_OF_CLASS_C = new Clazz("org.javaan.test.jar.ClassB");
	
	public static final Clazz ABSTACT_CLASS_A = new Clazz("org.javaan.test.jar.AbstractClassA");

	public static final Clazz SPECIALIZATION_CLASS_B = new Clazz("org.javaan.test.jar.SpecializationClassB");

	public static final Clazz CLASS_CALLING_ABSTACT_METHOD = new Clazz("org.javaan.test.jar.ClassCallingAbstractMethod");

	public static final Clazz CLASS_STRING = new Clazz("java.lang.String");
	
	public static final Clazz CLASS_EXTENDING_EXTERNAL_CLASS = new Clazz("org.javaan.test.jar.ClassExtendingExternalClass");

	public static final Clazz ABSTRACT_COLLECTION = new Clazz("java.util.AbstractCollection");

	public static final Interface INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceC");
	
	public static final Interface INTERFACE_B = new Interface("org.javaan.test.jar.InterfaceB");
	
	public static final Interface SUPER_INTERFACE1_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceB");
	
	public static final Interface SUPER_INTERFACE2_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceA");

	
	public static final String SIGNATURE_CONSTRUCTOR = "<init>()";
	
	public static final String SIGNATURE_METHOD_INTERFACE_B = "methodInterfaceB(java.lang.String,java.lang.String)";
	
	public static final String SIGNATURE_METHOD_CLASS_B = "methodClassB(org.javaan.test.jar.InterfaceC,byte)";
	
	public static final String SIGNATURE_ENTRY_METHOD = "entryMethod(org.javaan.test.jar.InterfaceB)";
	
	public static final String SIGNATURE_ABSTACT_METHOD = "abstractMethod()";
	
	public static final String SIGNATURE_CALL_ABSTRACT_METHID = "callAbstractMethod(org.javaan.test.jar.AbstractClassA)";

	public static final String SIGNATURE_CALL_EXTERNAL_CLASS = "methodCallingExternalClass()";

	public static final String SIGNATURE_METHOD_ABSTRACT_COLLECTION_SIZE = "size()";

	
	public static final Method CLASSA_METHOD_INTERFACE_B = new Method(CLASS_A, SIGNATURE_METHOD_INTERFACE_B);
	
	public static final Method CLASSB_METHOD_CLASS_B = new Method(CLASS_B, SIGNATURE_METHOD_CLASS_B);
	
	public static final Method CLASSC_ENTRY_METHOD = new Method(CLASS_C, SIGNATURE_ENTRY_METHOD);
	
	public static final Method CLASSB_CONSTRUCTOR = new Method(CLASS_B, SIGNATURE_CONSTRUCTOR);
	
	public static final Method ABSTRACT_CLASS_A_ABSTRACT_METHOD = new Method(ABSTACT_CLASS_A, SIGNATURE_ABSTACT_METHOD);

	public static final Method SPECIALIZATION_CLASS_B_ABSTRACT_METHOD = new Method(SPECIALIZATION_CLASS_B, SIGNATURE_ABSTACT_METHOD);

	public static final Method CLASS_CALLING_ABSTACT_METHOD_CALL_ABSTRACT_METHOD = new Method(CLASS_CALLING_ABSTACT_METHOD, SIGNATURE_CALL_ABSTRACT_METHID);

	public static final Method CLASSC_CALLING_EXTERNAL_CLASS = new Method(CLASS_C, SIGNATURE_CALL_EXTERNAL_CLASS);
	
	public static final Method STRING_CONSTRUCTOR = new Method(CLASS_STRING, SIGNATURE_CONSTRUCTOR);
}