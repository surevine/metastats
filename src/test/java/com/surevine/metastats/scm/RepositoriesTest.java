package com.surevine.metastats.scm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RepositoriesTest {
	
	@Test
	public void testCount() {
		assertEquals(31, Repositories.find().size());
	}
}
