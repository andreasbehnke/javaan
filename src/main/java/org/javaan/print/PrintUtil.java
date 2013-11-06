package org.javaan.print;

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
}
