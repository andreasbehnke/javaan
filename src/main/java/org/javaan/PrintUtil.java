package org.javaan;

import java.io.PrintStream;
import java.util.Collection;

public class PrintUtil {

	public static <E> void println(PrintStream output, Collection<E> elements, String separator) {
		StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (E e : elements) {
			if(first) {
				first = false;
			} else {
				buffer.append(separator);
			}
			buffer.append(e.toString());
		}
		output.println(buffer);
	}
}
