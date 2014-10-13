package com.surevine.metastats.util;

import java.io.File;
import java.io.IOException;

public abstract class FileTraversal {
	
	public final void traverse(final File f) throws IOException {
		if (f.isDirectory()) {
			if (onDirectory(f)) {
				final File[] childs = f.listFiles();
				for (File child : childs) {
					traverse(child);
				}
			}
		} else {
			onFile(f);
		}
	}

	public abstract boolean onDirectory(final File d);

	public abstract void onFile(final File f);
}
