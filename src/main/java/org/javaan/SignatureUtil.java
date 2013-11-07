package org.javaan;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Creates unique method signatures from methods or invocations
 */
public class SignatureUtil {

	public static String createSignature(Method method) {
		String methodName = method.getName();
		String signature = method.getSignature();
		return methodName + signature;
	}
	
	public static String createSignature(InvokeInstruction invoke, ConstantPoolGen cpg) {
		String methodName = invoke.getMethodName(cpg);
		String signature = invoke.getSignature(cpg);
		return methodName + signature;
	}
}
