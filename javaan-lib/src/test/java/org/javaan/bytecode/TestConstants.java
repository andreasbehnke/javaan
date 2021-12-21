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

	String TEST_JAR_FILE = "src/test/resources/testJar.jar";

	int NUMBER_OF_CLASSES = 7;

	int NUMBER_OF_EXTERNAL_CLASSES = 2;

	int NUMBER_OF_INTERFACES = 4;

	int NUMBER_OF_EXTERNAL_INTERFACES = 2;

	int NUMBER_OF_CLASSES_AND_INTERFACES = NUMBER_OF_CLASSES + NUMBER_OF_INTERFACES;


	Clazz CLASS_A = new Clazz("org.javaan.test.jar.ClassA");

	Clazz CLASS_B = new Clazz("org.javaan.test.jar.ClassB");

	Clazz CLASS_C = new Clazz("org.javaan.test.jar.ClassC");

	Clazz SUPER_CLASS_OF_CLASS_C = new Clazz("org.javaan.test.jar.ClassB");

	Clazz ABSTACT_CLASS_A = new Clazz("org.javaan.test.jar.AbstractClassA");

	Clazz SPECIALIZATION_CLASS_B = new Clazz("org.javaan.test.jar.SpecializationClassB");

	Clazz CLASS_CALLING_ABSTACT_METHOD = new Clazz("org.javaan.test.jar.ClassCallingAbstractMethod");

	Clazz CLASS_STRING = new Clazz("java.lang.String");

	Clazz CLASS_EXTENDING_EXTERNAL_CLASS = new Clazz("org.javaan.test.jar.ClassExtendingExternalClass");

	Clazz ABSTRACT_COLLECTION = new Clazz("java.util.AbstractCollection");

	Interface INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceC");

	Interface INTERFACE_B = new Interface("org.javaan.test.jar.InterfaceB");

	Interface SUPER_INTERFACE1_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceB");

	Interface SUPER_INTERFACE2_OF_INTERFACE_C = new Interface("org.javaan.test.jar.InterfaceA");


	String SIGNATURE_CONSTRUCTOR = "<init>()";

	String SIGNATURE_METHOD_INTERFACE_B = "methodInterfaceB(java.lang.String,java.lang.String)";

	String SIGNATURE_METHOD_CLASS_B = "methodClassB(org.javaan.test.jar.InterfaceC,byte)";

	String SIGNATURE_ENTRY_METHOD = "entryMethod(org.javaan.test.jar.InterfaceB)";

	String SIGNATURE_ABSTACT_METHOD = "abstractMethod()";

	String SIGNATURE_CALL_ABSTRACT_METHID = "callAbstractMethod(org.javaan.test.jar.AbstractClassA)";

	String SIGNATURE_CALL_EXTERNAL_CLASS = "methodCallingExternalClass()";

	String SIGNATURE_METHOD_ABSTRACT_COLLECTION_SIZE = "size()";


	Method CLASSA_METHOD_INTERFACE_B = new Method(CLASS_A, SIGNATURE_METHOD_INTERFACE_B);

	Method CLASSB_METHOD_CLASS_B = new Method(CLASS_B, SIGNATURE_METHOD_CLASS_B);

	Method CLASSC_ENTRY_METHOD = new Method(CLASS_C, SIGNATURE_ENTRY_METHOD);

	Method CLASSB_CONSTRUCTOR = new Method(CLASS_B, SIGNATURE_CONSTRUCTOR);

	Method ABSTRACT_CLASS_A_ABSTRACT_METHOD = new Method(ABSTACT_CLASS_A, SIGNATURE_ABSTACT_METHOD);

	Method SPECIALIZATION_CLASS_B_ABSTRACT_METHOD = new Method(SPECIALIZATION_CLASS_B, SIGNATURE_ABSTACT_METHOD);

	Method CLASS_CALLING_ABSTACT_METHOD_CALL_ABSTRACT_METHOD = new Method(CLASS_CALLING_ABSTACT_METHOD, SIGNATURE_CALL_ABSTRACT_METHID);

	Method CLASSC_CALLING_EXTERNAL_CLASS = new Method(CLASS_C, SIGNATURE_CALL_EXTERNAL_CLASS);

	Method STRING_CONSTRUCTOR = new Method(CLASS_STRING, SIGNATURE_CONSTRUCTOR);
}
