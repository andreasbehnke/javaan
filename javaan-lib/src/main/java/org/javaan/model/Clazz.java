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

@SuppressWarnings("rawtypes")
public class Clazz extends Type {

    private final List<String> interfaceNames;

    private final String superTypeName;

    public Clazz(String name) {
		super(name);
        this.superTypeName = null;
		this.interfaceNames = null;
	}

    public Clazz(String name, String superTypeName, List<String> interfaceNames) {
        super(name);
        this.interfaceNames = interfaceNames;
        this.superTypeName = superTypeName;
    }

    protected Clazz(JavaClass javaClass, String filePath) {
		super(javaClass, filePath);
        this.superTypeName = javaClass.getSuperclassName();
		this.interfaceNames = Arrays.asList(javaClass.getInterfaceNames());
	}

    protected Clazz(Class clazz) {
		super(clazz);
		if (clazz.getSuperclass() != null) {
            this.superTypeName = clazz.getSuperclass().getName();
        } else {
		    this.superTypeName = null;
        }
        this.interfaceNames = new ArrayList<>();
        Arrays.stream(clazz.getInterfaces()).forEach( i -> interfaceNames.add(i.getName()) );
    }

	@Override
	public JavaType getJavaType() {
		return JavaType.CLASS;
	}

    public List<String> getInterfaceNames() {
        return interfaceNames;
    }

    public String getSuperTypeName() {
        return superTypeName;
    }
}
