package com.surevine.metastats.api.model.comparator;

import java.util.Comparator;

import com.surevine.metastats.api.model.User;

public class UserActivityComparator implements Comparator<User> {

	@Override
	public int compare(final User o1, final User o2) {
		if (o1.getNumCommits() < o2.getNumCommits()) return -1;
		else if (o1.getNumCommits() > o2.getNumCommits()) return 1;
		else return 0;
	}
}
