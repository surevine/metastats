package com.surevine.metastats.api.user;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.surevine.metastats.api.model.User;

public class UserTest {

	@Test
	public void testMerge() {
		final User a = new User();
		final User b = new User();

		a.getPunchCard()[2][2] = 2;
		b.getPunchCard()[2][2] = 3;
		
		a.merge(b);
		
		assertEquals(5, a.getPunchCard()[2][2]);
	}
}
