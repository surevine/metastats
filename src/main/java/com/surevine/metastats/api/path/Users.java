package com.surevine.metastats.api.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.Cache;

import com.surevine.metastats.api.model.User;
import com.surevine.metastats.api.model.comparator.UserActivityComparator;
import com.surevine.metastats.cache.CommunityCache;

@Path("/users")
public class Users {
	
	private static final int MAX_ACTIVE = 10;
	
	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers() {
		return CommunityCache.getInstance().getUsers();
	}

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/active")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getActiveUsers() {
		final ArrayList<User> uberUserList = new ArrayList<User>(getAllUsers());
		
		Collections.sort(uberUserList, new UserActivityComparator());
		Collections.reverse(uberUserList);
		
		return uberUserList.subList(0, Math.min(MAX_ACTIVE, uberUserList.size()));
	}

	@GET
	@Cache(isPrivate=true, maxAge=3600)
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") final String id) {
		return CommunityCache.getInstance().getUser(id);
	}
}
