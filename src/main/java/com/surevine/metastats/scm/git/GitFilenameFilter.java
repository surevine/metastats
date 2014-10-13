package com.surevine.metastats.scm.git;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class GitFilenameFilter implements FileFilter {

	@Override
	public boolean accept(final File repository) {
		return repository.isDirectory() && Arrays.asList(repository.list()).contains(".git");
	}
}
