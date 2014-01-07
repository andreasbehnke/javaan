package org.javaan.model;

public class Package extends NamedObjectBase {
	
	public static final String DEFAULT_PACKAGE_NAME = "";

	public Package(String name) {
		super(name);
	}
	
	public Package(Type type) {
		super(getPackageNameOfType(type));
	}
	
	public static String getPackageNameOfType(Type type) {
		String typeName = type.getName();
		int indexOfLastPoint = typeName.lastIndexOf('.');
		if (indexOfLastPoint == -1) {
			return DEFAULT_PACKAGE_NAME;
		}
		return typeName.substring(0, indexOfLastPoint);
	}
}
