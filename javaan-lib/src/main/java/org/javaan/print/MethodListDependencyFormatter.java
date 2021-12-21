package org.javaan.print;

import java.util.ArrayList;
import java.util.List;

import org.javaan.model.Dependency;
import org.javaan.model.Method;

public class MethodListDependencyFormatter implements ObjectFormatter<Dependency> {

	private static final String MORE = "...";

	private final int maxNumberOfMethods;

	public MethodListDependencyFormatter(int maxNumberOfMethods) {
		this.maxNumberOfMethods = maxNumberOfMethods;
	}

	@Override
	public String format(Dependency dependency) {
		StringBuilder buffer = new StringBuilder();
		List<Method> methods = new ArrayList<>(dependency.getCallee());
		int count = methods.size();
		count = Math.min(count, maxNumberOfMethods);
		for (int i=0; i < count; i++) {
			buffer.append(methods.get(i).getMethodName()).append(System.lineSeparator());
		}
		if (count < methods.size()) {
			buffer.append(MORE);
		}
		return buffer.toString();
	}

}
