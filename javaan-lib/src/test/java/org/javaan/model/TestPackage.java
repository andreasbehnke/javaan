package org.javaan.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestPackage {

	@Test
	public void testGetPackageNameOfType() {
		assertEquals("a.b.c", Package.getPackageNameOfType(new Clazz("a.b.c.A")));
		assertEquals(Package.DEFAULT_PACKAGE_NAME, Package.getPackageNameOfType(new Clazz("B")));
	}
}
