package org.javaan;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.Options;
import org.junit.Test;

public class TestJavaanCli {
	
	private static abstract class CommandStub implements Command {
		
		@Override
		public Options buildCommandLineOptions(Options options) {
			options.addOption("t", "test", false, "Just a test");
			return options;
		}
	}
	
	@Test
	public void testExecuteParseError() {
		assertEquals(ReturnCodes.errorParse, new JavaanCli(new String[]{}, new CommandMap()).execute());
		assertEquals(ReturnCodes.errorParse, new JavaanCli(new String[]{"unknownCommand"}, new CommandMap()).execute());
		assertEquals(ReturnCodes.errorParse, new JavaanCli(new String[]{"unknownCommand", "file1"}, new CommandMap()).execute());
	}
	
	@Test
	public void testExecuteParseErrorUnkownOption() {
		CommandMap commands = new CommandMap();
		Command command = mock(CommandStub.class);
		doCallRealMethod().when(command).buildCommandLineOptions(any(Options.class));
		when(command.getName()).thenReturn("test");
		when(command.getHelpCommandLine()).thenReturn("javaan test");
		commands.addCommand(command);
		assertEquals(ReturnCodes.errorParse, new JavaanCli(new String[]{"test", "--unknownOption"}, commands).execute());
		verify(command).getHelpCommandLine();
	}
	
	@Test
	public void testExecuteCommandError() {
		CommandMap commands = new CommandMap();
		Command command = mock(CommandStub.class);
		when(command.getName()).thenReturn("test");
		doCallRealMethod().when(command).buildCommandLineOptions(any(Options.class));
		doThrow(new RuntimeException("error")).when(command).execute(any(CommandContext.class));
		commands.addCommand(command);
		assertEquals(ReturnCodes.errorCommand, new JavaanCli(new String[]{"test", "file1"}, commands).execute());
		verify(command).execute(any(CommandContext.class));
	}

	@Test
	public void testExecuteCommandSpecificHelp() {
		CommandMap commands = new CommandMap();
		Command command = mock(CommandStub.class);
		doCallRealMethod().when(command).buildCommandLineOptions(any(Options.class));
		when(command.getName()).thenReturn("test");
		when(command.getHelpCommandLine()).thenReturn("javaan test");
		commands.addCommand(command);
		assertEquals(ReturnCodes.ok, new JavaanCli(new String[]{"test", "--help"}, commands).execute());
		verify(command).getHelpCommandLine();
	}
	
	@Test
	public void testExecuteHelpWithoutCommand() {
		CommandMap commands = new CommandMap();
		assertEquals(ReturnCodes.ok, new JavaanCli(new String[]{"--help"}, commands).execute());
	}
}
