package org.javaan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.javaan.model.Clazz;
import org.javaan.model.Interface;
import org.javaan.model.NamedObjectRepository;
import org.javaan.model.Type;
import org.junit.Test;

/**
 * This test makes sure that the method signatures created from
 * methods are the same signatures created from invoke instructions.
 */
public class TestSignatureUtil implements TestConstants {
	
	private List<Type> loadClasses() throws IOException {
		return new JarFileLoader().loadJavaClasses(new String[]{TEST_JAR_FILE});
	} 
	
	@Test
	public void testCreateSignatureFromMethod() throws IOException {
		NamedObjectRepository<Type> types = new NamedObjectRepository<Type>(loadClasses());
		Interface i = (Interface)types.get(INTERFACE_B.getName());
		Method method = i.getJavaClass().getMethods()[0]; /* public String methodInterfaceB(String a, String b); */
		assertEquals(SIGNATURE_METHOD_INTERFACE_B, SignatureUtil.createSignature(method));
	}
	
	@Test
	public void testCreateSignatureFromInvoke() throws IOException {
		NamedObjectRepository<Type> types = new NamedObjectRepository<Type>(loadClasses());
		Clazz c = (Clazz)types.get(CLASS_B.getName());
		ConstantPoolGen cpg = new ConstantPoolGen(c.getJavaClass().getConstantPool());
		Method method = c.getJavaClass().getMethods()[1]; /* void methodClassB(InterfaceC c); */
		MethodGen mg = new MethodGen(method, c.getName(), cpg);
        InvokeInstruction invoke = null;
		for (InstructionHandle ih = mg.getInstructionList().getStart(); ih != null; ih = ih.getNext()) {
            Instruction i = ih.getInstruction();
            if (i instanceof InvokeInstruction) {
            	invoke = (InvokeInstruction)i;
            }
        }
		assertNotNull(invoke);
		assertEquals(SIGNATURE_METHOD_INTERFACE_B, SignatureUtil.createSignature(invoke, cpg));
	}
}