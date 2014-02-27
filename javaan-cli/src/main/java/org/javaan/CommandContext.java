package org.javaan;

import org.apache.commons.cli.CommandLine;
import org.javaan.commands.StandardOptions;

/**
 * Provides all information needed to run a specific command.
 */
public class CommandContext {
	
	public enum AdditionalTypeInformation {
		SUPER_CLASSES,
		SPECIALIZATIONS,
		INTERFACES,
		IMPLEMENTATIONS,
		METHODS,
		VIRTUAL_METHODS,
		NONE
	}

	private final CommandLine commandLine;
	
	private final String[] arguments;
	
	private final Settings settings;
	
	private ReturnCodes returnCode = ReturnCodes.ok;

	public CommandContext(CommandLine commandLine, String[] arguments, Settings settings) {
		this.commandLine = commandLine;
		this.arguments = arguments;
		this.settings = settings;
	}
	
	public String[] getArguments() {
		return arguments;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public ReturnCodes getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCodes returnCode) {
		this.returnCode = returnCode;
	}
	
	public AdditionalTypeInformation getAdditionalTypeInformation() {
		if (commandLine.hasOption(StandardOptions.OPT_SUPER)) {
			return AdditionalTypeInformation.SUPER_CLASSES;
		} else if (commandLine.hasOption(StandardOptions.OPT_SPECIALIZATIONS)) {
			return AdditionalTypeInformation.SPECIALIZATIONS;
		} else if (commandLine.hasOption(StandardOptions.OPT_INTERFACES)) {
			return AdditionalTypeInformation.INTERFACES;
		} else if (commandLine.hasOption(StandardOptions.OPT_IMPLEMENTATIONS)) {
			return AdditionalTypeInformation.IMPLEMENTATIONS;
		} else if (commandLine.hasOption(StandardOptions.OPT_METHODS)) {
			return AdditionalTypeInformation.METHODS;
		} else if (commandLine.hasOption(StandardOptions.OPT_VIRTUAL_METHODS)) {
			return AdditionalTypeInformation.VIRTUAL_METHODS;
		} else {
			return AdditionalTypeInformation.NONE;
		}
	}
	
	public boolean hasFilterCriteria() {
		return commandLine.hasOption(StandardOptions.OPT_FILTER);
	}
	
	public String getFilterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	public String getMethodFilterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_METHOD);
	}

	public boolean isPrintLeaves() {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}
	
	public boolean isResolveDependenciesInClassHierarchy() {
		if (commandLine.hasOption(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY)) {
			return true;
		} else {
			return settings.isResolveDependenciesInClassHierarchy();
		}
	}
	
	public boolean isResolveMethodImplementations() {
		if (commandLine.hasOption(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS)) {
			return true;
		} else {
			return settings.isResolveMethodImplementations();
		}
	}

	public boolean isDisplay2dGraph() {
		if (commandLine.hasOption(StandardOptions.OPT_DISPLAY_2D_GRAPH)) {
			return true;
		} else {
			return settings.isDisplay2dGraph();
		}
	}
}
