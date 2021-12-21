package org.javaan.print;

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

import org.javaan.model.Method;

public class MethodFormatter implements ObjectFormatter<Method> {

	private static final String DEFAULT_METHOD_FORMAT = "%1$s%2$s.%3$s(%4$s)";

	private final String format = DEFAULT_METHOD_FORMAT;

	@Override
	public String format(Method method) {
		String parameters = PrintUtil.createArgumentList(method.getParamterTypes());
		return String.format(format, "[M]", method.getType().getName(), method.getMethodName(), parameters);
	}

}
