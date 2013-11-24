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

import java.io.PrintStream;
import java.util.Collection;

public class PrintUtil {
	
	public static <E> void println(PrintStream output, Collection<E> elements, String prefix, String linePrefix, String separator) {
		println(output, null, elements, prefix, linePrefix, separator);
	}
	
	public static <E> void println(PrintStream output, ObjectFormatter<E> formatter, Collection<E> elements, String prefix, String linePrefix, String separator) {
		output.print(prefix);
		boolean first = true;
		for (E e : elements) {
			if(first) {
				first = false;
			} else {
				output.print(separator);
			}
			output.print(linePrefix);
			if (formatter == null) {
				output.print(e.toString());
			} else {
				output.print(formatter.format(e));
			}
		}
		output.println();
	}

	public static final String BLOCK_SEPARATOR = "\n--\n";
}
