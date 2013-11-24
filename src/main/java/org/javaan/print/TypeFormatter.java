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

import org.javaan.model.Type;

public class TypeFormatter implements ObjectFormatter<Type> {

	@Override
	public String format(Type type) {
		switch (type.getJavaType()) {
		case CLASS:
			return "[C]" + type.getName();
		case INTERFACE:
			return "[I]" + type.getName();
		default:
			throw new IllegalArgumentException("Unknown java type: " + type); // this should never happen
		}
	}
}
