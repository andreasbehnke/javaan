package org.javaan;

import java.io.PrintStream;
import java.util.Collection;

public class PrintUtil {

	public static <E> void println(PrintStream output, Collection<E> elements, String prefix, String linePrefix, String separator) {
		output.print(prefix);
		boolean first = true;
		for (E e : elements) {
			if(first) {
				first = false;
			} else {
				output.print(separator);
			}
			output.print(linePrefix);
			output.print(e.toString());
		}
		output.println();
	}
}
