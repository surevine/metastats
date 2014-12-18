package com.surevine.metastats.api.path;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Data;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.jboss.resteasy.annotations.cache.Cache;

import com.surevine.metastats.scm.GenericRepository;
import com.surevine.metastats.scm.Repositories;
import com.surevine.metastats.util.ConfigurationProperties;

@Path("/init")
public class Init {

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public InitData getInit() {
		final List<GenericRepository> repositories = Repositories.find();
		
		final InitData data = new InitData();
		
		data.setRepositoryCount(repositories.size());
		for (final GenericRepository repo : repositories) {
			data.setCommitCount(data.getCommitCount() + repo.getCommitCount());
			data.setContributorCount(data.getContributorCount() + repo.getUsers().size());
			data.setFileCount(data.getFileCount() + repo.getFileCount());
			data.setLinesOfCode(data.getLinesOfCode() + repo.getLinesOfCode());
		}
		
		return data;
	}
	
	@GET
	@Path("/reload")
	public void reload() {
		Repositories.load();
	}
	
	@POST
	@Path("/add")
	@Consumes("application/x-www-form-urlencoded")
	public void add(@FormParam("url") String url) throws UnsupportedEncodingException {
		System.out.println("Cloning: "+url);
		url = URLDecoder.decode(url, "UTF-8");
		final File root = new File(ConfigurationProperties.get(ConfigurationProperties.REPOSITORY_CACHE).concat(url.substring(url.lastIndexOf('/'), url.length()-4)));
		System.out.println("Cloning into "+root.getAbsolutePath());
		try {
			Git.cloneRepository()
				.setURI(url)
				.setDirectory(root)
				.setBare(false)
				.setCloneAllBranches(false)
				.call();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
	
	@Data
	public class InitData {
		private long repositoryCount = 0;
		private long commitCount = 0;
		private long contributorCount = 0;
		private long linesOfCode = 0;
		private long fileCount = 0;
	}
}
