package com.surevine.metastats.scm;

import java.io.File;
import java.util.List;

import org.jboss.resteasy.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult.MergeStatus;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

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
	
	public static void load() {
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
					Repository repository = new RepositoryBuilder().findGitDir(root).build();
					new Git(repository).pull().call();
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
	
	public static void update() {
		final long start = System.currentTimeMillis();
		LOG.info("Loading repositories...");

		final File cache = new File(ConfigurationProperties.get(ConfigurationProperties.REPOSITORY_CACHE));
		final File[] repositoryRoots = cache.listFiles(new GitFilenameFilter());
		
		if (repositoryRoots!=null && repositoryRoots.length>0) {
			for (final File root : repositoryRoots) {
				LOG.info("Updating repository "+root);
				try {
					Repository repository = new RepositoryBuilder().findGitDir(root).build();
					PullResult result = new Git(repository).pull().call();
					
					if(!result.getMergeResult().getMergeStatus().equals(MergeStatus.ALREADY_UP_TO_DATE)) {
						LOG.info("Found update - reindexing");
						CommunityCache.getInstance().addRepository(new GitRepository(root));
					} else {
						LOG.info("Not updated");
					}
				}
				catch (Exception e) {
					LOG.warn("Error updating repository at :"+root, e);
				}
			}
		}
		else {
			LOG.info("No repositories to update");
		}
		
		LOG.info(String.format("...updated in %dms.",
				(System.currentTimeMillis() - start)));
		
	}
}
