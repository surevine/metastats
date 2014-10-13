package com.surevine.metastats.api.model.comparator;

import java.util.Comparator;

import com.surevine.metastats.api.model.Project;

public class ProjectActivityComparator implements Comparator<Project> {

	@Override
	public int compare(final Project o1, final Project o2) {
		if (o1.getNumCommits() < o2.getNumCommits()) return -1;
		else if (o1.getNumCommits() > o2.getNumCommits()) return 1;
		else return 0;
	}
}
