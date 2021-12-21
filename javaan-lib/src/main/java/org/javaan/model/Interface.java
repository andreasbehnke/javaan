package org.javaan.model;

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

import org.apache.bcel.classfile.JavaClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interface extends Type {

    private final List<String> superInterfaceNames;

    public Interface(String name) {
		super(name);
        superInterfaceNames = null;
	}

	public Interface(String name, List<String> superInterfaceNames) {
		super(name);
		this.superInterfaceNames = superInterfaceNames;
	}

	protected Interface(JavaClass javaClass, String filePath) {
		super(javaClass, filePath);
        superInterfaceNames = Arrays.asList(javaClass.getInterfaceNames());
	}

	@SuppressWarnings("rawtypes")
	protected Interface(Class clazz) {
		super(clazz);
		superInterfaceNames = new ArrayList<>();
        Arrays.stream(clazz.getInterfaces()).forEach( i -> superInterfaceNames.add(i.getName()) );
	}

	@Override
	public JavaType getJavaType() {
		return JavaType.INTERFACE;
	}

    public List<String> getSuperInterfaceNames() {
        return superInterfaceNames;
    }
}
