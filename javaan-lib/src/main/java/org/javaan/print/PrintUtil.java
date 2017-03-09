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

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

public class PrintUtil {

	static final String LINE_SEPARATOR = System.lineSeparator();

	static final String BLOCK_SEPARATOR = LINE_SEPARATOR + "--" + LINE_SEPARATOR;

	static final String LEVEL_SPACER = " ";

	public static <E> void println(Writer writer, Collection<E> elements, String prefix, String linePrefix, String separator) {
		println(writer, null, elements, prefix, linePrefix, separator);
	}
	
	public static <F> void println(Writer writer, ObjectFormatter<F> formatter, Collection<F> elements, String prefix, String linePrefix, String separator) {
		try {
            writer.write(prefix);
            boolean first = true;
            for (F e : elements) {
                if (first) {
                    first = false;
                } else {
                    writer.write(separator);
                }
                writer.write(linePrefix);
                if (formatter == null) {
                    writer.write(e.toString());
                } else {
                    writer.write(formatter.format(e));
                }
            }
            writer.write(LINE_SEPARATOR);
            writer.flush();
        } catch (IOException ioe) {
		    throw new RuntimeException(ioe);
        }
	}

	public static void println(Writer writer) {
	    try {
	        writer.write(LINE_SEPARATOR);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void println(Writer writer, String line) {
        try {
            writer.append(line).write(LINE_SEPARATOR);
            writer.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void print(Writer writer, String line) {
        try {
            writer.append(line).flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
	
	public static <F> void indent(Writer writer, ObjectFormatter<F> formatter, F element, int indentWidth) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i < indentWidth; i++) {
			buffer.append(LEVEL_SPACER);
		}
        try {
            writer.append(buffer).append(formatter.format(element)).write(LINE_SEPARATOR);
            writer.flush();
        } catch (IOException ioe) {
		    throw new RuntimeException(ioe);
        }
    }
	
	public static void printSeparator(Writer writer) {
        try {
            writer.append(PrintUtil.BLOCK_SEPARATOR).write(LINE_SEPARATOR);
            writer.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void format(Writer writer, String format, Object... args) {
        try {
            writer.append(String.format(format, args)).write(LINE_SEPARATOR);
            writer.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
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