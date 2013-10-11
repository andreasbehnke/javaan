package org.javaan;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Javaan Command Line Client
 * @author behnkea
 */
public class JavaanCli {
	
	public static void main(String[] args) {
		Options options = createOptions();
		try {
			CommandLine cmd = new GnuParser().parse(createOptions(), args);
			cmd.getArgList();
		} catch (ParseException e) {
			printUsage(options);
		}	
	}

	private static Options createOptions() {
		Options options = new Options();
		return options;
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "javaan [command] ", options);
	}
}