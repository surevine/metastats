package com.surevine.metastats.api.path;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.surevine.metastats.api.path.Init.InitData;

public class InitTest {

	@Test
	public void testGetInit() {
		final InitData init = new Init().getInit();

		System.out.println("Commit count: " +init.getCommitCount());
		System.out.println("Contributor count: " +init.getContributorCount());
		System.out.println("File count: " +init.getFileCount());
		System.out.println("Lines of code: " +init.getLinesOfCode());
		
		assertEquals(31, init.getRepositoryCount());
	}
}
