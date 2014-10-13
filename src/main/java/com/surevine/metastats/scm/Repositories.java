package com.surevine.metastats.scm;

import java.io.File;
import java.util.List;

import org.jboss.resteasy.logging.Logger;

import com.surevine.metastats.cache.CommunityCache;
import com.surevine.metastats.scm.git.GitFilenameFilter;
import com.surevine.metastats.scm.git.GitRepository;
import com.surevine.metastats.util.ConfigurationProperties;

public class Repositories {
	
	private static final Logger LOG = Logger.getLogger(Repositories.class);
	
	static {
		load();
	}

	public static List<GenericRepository> find() {
		return CommunityCache.getInstance().getRepositories();
	}
	
	public static GenericRepository find(final String name) {
		return CommunityCache.getInstance().getRepository(name);
	}
	
	private static void load() {
		final long start = System.currentTimeMillis();
		LOG.info("Loading repositories...");
		
		final File cache = new File(ConfigurationProperties.get(ConfigurationProperties.REPOSITORY_CACHE));
		final File[] repositoryRoots = cache.listFiles(new GitFilenameFilter());
		
		// deploy next
		
		if (repositoryRoots!=null && repositoryRoots.length>0) {
			for (final File root : repositoryRoots) {
				LOG.info("Loading repository "+root);
				try {
					CommunityCache.getInstance().addRepository(new GitRepository(root));
				}
				catch (Exception e) {
					LOG.warn("Error adding repository at :"+root, e);
				}
			}
		}
		else {
			LOG.info("No repositories to load");
		}
		
		LOG.info(String.format("...loaded in %dms.",
				(System.currentTimeMillis() - start)));
	}
}