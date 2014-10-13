package com.surevine.metastats.api.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.Cache;

import com.surevine.metastats.api.model.Project;
import com.surevine.metastats.api.model.comparator.ProjectActivityComparator;
import com.surevine.metastats.scm.GenericRepository;
import com.surevine.metastats.scm.Repositories;

@Path("/projects")
public class Projects {
	
	private static final int MAX_ACTIVE = 10;

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProjects() {
		return "{\"name\":\"TPSC\", \"children\": [{\"name\":\"git\"},{\"name\":\"linux\"},{\"name\":\"gitlab\"},{\"name\":\"git-labelling\"},{\"name\":\"metastats\"},{\"name\":\"firefox\"},{\"name\":\"chromium\"},{\"name\":\"buddycloud\"},{\"name\":\"d3\"},{\"name\":\"Apache HTTP Server\"}]}";
	}

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> getAllProjects() {
		final List<GenericRepository> repos = Repositories.find();
		
		final List<Project> projects = new ArrayList<Project>(repos.size());
		for (final GenericRepository repo : repos) {
			projects.add(Project.from(repo));
		}
		
		return projects;
	}

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> getActiveProjects() {
		final List<Project> projects = getAllProjects();
		
		Collections.sort(projects, new ProjectActivityComparator());
		Collections.reverse(projects);
		
		return projects.subList(0, Math.min(MAX_ACTIVE, projects.size()));
	}

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/named/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Project getProject(@PathParam("name") final String name) {
		return Project.from(Repositories.find(name));
	}
}
