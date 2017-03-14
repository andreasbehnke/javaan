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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.NamedObjectMap;
import org.javaan.model.Type;
import org.junit.Test;

/**
 * This test makes sure that the method signatures created from
 * methods are the same signatures created from invoke instructions.
 */
public class TestSignatureUtil implements TestConstants {
	
	private List<Type> loadClasses() throws IOException {
		return new JarFileLoader(true).loadJavaClasses(new String[]{TEST_JAR_FILE});
	}
	
	@Test
	public void testCreateMethodSignatureFromClassMethod() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Class<?> clazz = Class.forName("java.lang.Byte");
		java.lang.reflect.Method compareMethod = clazz.getMethod("compare", byte.class, byte.class);
		assertEquals("compare(byte,byte)", SignatureUtil.createMethodSignature(compareMethod));
	}
	
	@Test
	public void testCreateMethodSignatureFromClassConstructor() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		Class<?> clazz = Class.forName("java.lang.String");
		Constructor<?> constructor = clazz.getConstructor();
		assertEquals(SIGNATURE_CONSTRUCTOR, SignatureUtil.createMethodSignature(constructor));
	}
	
	
	@Test
	public void testCreateMethodSignatureFromMethod() throws IOException {
		NamedObjectMap<Type> types = new NamedObjectMap<Type>(loadClasses());
		Interface i = (Interface)types.get(INTERFACE_B.getName());
		Method method = i.getJavaClass().getMethods()[0]; /* public String methodInterfaceB(String a, String b); */
		assertEquals(SIGNATURE_METHOD_INTERFACE_B, SignatureUtil.createMethodSignature(method));
	}
	
	private InvokeInstruction getFirstInvokeInstruction(MethodGen mg) {
		InvokeInstruction invoke = null;
		for (InstructionHandle ih = mg.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
            Instruction i = ih.getInstruction();
            if (i instanceof InvokeInstruction) {
            	invoke = (InvokeInstruction)i;
            }
        }
		assertNotNull(invoke);
		return invoke;
	}
	
	@Test
	public void testCreateSignatureFromInvoke() throws IOException {
		NamedObjectMap<Type> types = new NamedObjectMap<Type>(loadClasses());
		Clazz classb = (Clazz)types.get(CLASS_B.getName());
		ConstantPoolGen cpg = new ConstantPoolGen(classb.getJavaClass().getConstantPool());
		Method method = classb.getJavaClass().getMethods()[1]; /* void methodClassB(InterfaceC c); */
		MethodGen mg = new MethodGen(method, classb.getName(), cpg);
		InvokeInstruction invoke = getFirstInvokeInstruction(mg);

		assertEquals(SIGNATURE_METHOD_INTERFACE_B, SignatureUtil.createMethodSignature(invoke, cpg));
		
		// signature of constructor invoke
		Clazz classc = (Clazz)types.get(CLASS_C.getName());
		cpg = new ConstantPoolGen(classc.getJavaClass().getConstantPool());
		method = classc.getJavaClass().getMethods()[2]; /* public void methodCallingExternalClass() { ... */
		mg = new MethodGen(method, classc.getName(), cpg);
        invoke = getFirstInvokeInstruction(mg);
        
        assertEquals(SIGNATURE_CONSTRUCTOR, SignatureUtil.createMethodSignature(invoke, cpg));
	}
}