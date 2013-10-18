package org.javaan;

import org.apache.bcel.classfile.JavaClass;

public class ClassData {
	
	private final JavaClass clazz;
	
	private final String filePath;
	
	public ClassData(final String filePath, final JavaClass clazz) {
		this.clazz = clazz;
		this.filePath = filePath;
	}

	public JavaClass getJavaClass() {
		return clazz;
	}

	public String getClassName() {
		return clazz.getClassName();
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s]", clazz.getClassName(), filePath);
	}
}
