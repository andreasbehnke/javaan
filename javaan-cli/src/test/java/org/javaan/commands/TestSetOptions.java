package org.javaan.commands;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.javaan.CommandContext;
import org.javaan.ReturnCodes;
import org.javaan.Settings;
import org.junit.Test;

public class TestSetOptions {
	
	@Test
	public void testNoOptionsSet() {
		Settings settings = new Settings("/" + UUID.randomUUID());
		CommandLine commandLine = mock(CommandLine.class);
		when(commandLine.hasOption(anyString())).thenReturn(false);
		CommandContext context = new CommandContext(commandLine, new String[]{}, settings);
		
		assertFalse(context.isResolveDependenciesInClassHierarchy());
		assertFalse(context.isResolveMethodImplementations());
		assertFalse(context.isDisplay2dGraph());
		
		commandLine = mock(CommandLine.class);
		when(commandLine.hasOption(anyString())).thenReturn(true);
		context = new CommandContext(commandLine, new String[]{}, settings);
		
		assertTrue(context.isResolveDependenciesInClassHierarchy());
		assertTrue(context.isResolveMethodImplementations());
		assertTrue(context.isDisplay2dGraph());
	}

	@Test
	public void testSetOptions() {
		Settings settings = new Settings("/" + UUID.randomUUID());
		CommandLine commandLine = mock(CommandLine.class);
		when(commandLine.hasOption(anyString())).thenReturn(false);
		CommandContext context = new CommandContext(commandLine, new String[]{StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY}, settings);
		assertEquals(ReturnCodes.ok, new SetOptions().execute(context));
		
		assertTrue(context.isResolveDependenciesInClassHierarchy());
		assertFalse(context.isResolveMethodImplementations());
		assertFalse(context.isDisplay2dGraph());
		
		context = new CommandContext(commandLine, new String[]{StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS}, settings);
		assertEquals(ReturnCodes.ok, new SetOptions().execute(context));
		
		assertTrue(context.isResolveDependenciesInClassHierarchy());
		assertTrue(context.isResolveMethodImplementations());
		assertFalse(context.isDisplay2dGraph());
		
		context = new CommandContext(commandLine, new String[]{StandardOptions.OPT_DISPLAY_2D_GRAPH}, settings);
		assertEquals(ReturnCodes.ok, new SetOptions().execute(context));
		
		assertTrue(context.isResolveDependenciesInClassHierarchy());
		assertTrue(context.isResolveMethodImplementations());
		assertTrue(context.isDisplay2dGraph());
	}
}