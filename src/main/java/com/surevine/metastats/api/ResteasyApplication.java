package com.surevine.metastats.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.surevine.metastats.api.path.Init;
import com.surevine.metastats.api.path.Projects;
import com.surevine.metastats.api.path.Users;

public class ResteasyApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>(2);
	
	public ResteasyApplication() {
		singletons.add(new Projects());
		singletons.add(new Users());
		singletons.add(new Init());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
