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
import java.util.List;
public class PrintUtil {

	static final String BLOCK_SEPARATOR = "\n--\n";

	static final String LEVEL_SPACER = " ";

	public static <E> void println(PrintStream output, Collection<E> elements, String prefix, String linePrefix, String separator) {
		println(output, null, elements, prefix, linePrefix, separator);
	}
	
	public static <F> void println(PrintStream output, ObjectFormatter<F> formatter, Collection<F> elements, String prefix, String linePrefix, String separator) {
		output.print(prefix);
		boolean first = true;
		for (F e : elements) {
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
	
	public static <F> void indent(PrintStream output, ObjectFormatter<F> formatter, F element, int indentWidth) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i < indentWidth; i++) {
			buffer.append(LEVEL_SPACER);
		}
		output.append(buffer).append(formatter.format(element)).println();
	}
	
	public static void printSeparator(PrintStream output) {
		output.println(PrintUtil.BLOCK_SEPARATOR);
	}
	
	public static String createArgumentList(List<String> args) {
		if (args == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (String arg : args) {
			if (first) {
				first = false;
			} else {
				buffer.append(',');
			}
			buffer.append(arg);
		}
		return buffer.toString();
	}
}