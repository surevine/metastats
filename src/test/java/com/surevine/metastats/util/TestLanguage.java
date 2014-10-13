package com.surevine.metastats.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestLanguage {

	@Test
	public void testLanguages() {
		assertEquals("Java", Language.get("Test.java"));
		
		assertEquals(null, Language.get(".x"));
	}
}
