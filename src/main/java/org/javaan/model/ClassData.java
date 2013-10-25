package org.javaan.model;

import org.apache.bcel.classfile.JavaClass;

public class ClassData implements Comparable<ClassData> {
	
	private final JavaClass clazz;
	
	private final String className;
	
	private final String filePath;
	
	public ClassData(final String filePath, final JavaClass clazz) {
		this.clazz = clazz;
		this.className = clazz.getClassName();
		this.filePath = filePath;
	}

	public JavaClass getJavaClass() {
		return clazz;
	}

	public String getClassName() {
		return className;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s]", className, filePath);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassData other = (ClassData) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ClassData o) {
		return className.compareTo(o.className);
	}
}
