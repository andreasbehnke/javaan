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

public enum ReturnCodes {
	
	ok(0),
	errorParse(1),
	errorCommand(2),
	// this code signals that another thread has been spawn and
	// the JVM should not be stopped
	threadSpawn(3);

	private final int value;
	
	private ReturnCodes(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
