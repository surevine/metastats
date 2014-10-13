package com.surevine.metastats.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Map.Entry;

import com.surevine.metastats.api.model.User;
import com.surevine.metastats.scm.GenericRepository;

public class CommunityCache {
	private static final CommunityCache INSTANCE;
	
	static {
		INSTANCE = new CommunityCache();
	}

	private Map<String, GenericRepository> repoCache;
	private Map<String, User> userCache;
	
	private CommunityCache() {
		repoCache=new HashMap<String, GenericRepository>();
		userCache = new HashMap<String, User>();
	}
	
	public void addUsers(final Collection<User> users) {
		for (final User user : users) {
			addUser(user);
		}
	}
	
	public void addRepositories(final Collection<GenericRepository> repositories) {
		for (final GenericRepository repository : repositories) {
			addRepository(repository);
		}
	}
	
	public void addUser(final User user) {
		if (userCache.containsKey(user.getEmail())) {
			user.merge(userCache.get(user.getEmail()));
		}
		
		userCache.put(user.getEmail(), user);
	}
	
	public void addRepository(final GenericRepository repository) {
		repoCache.put(repository.getName(), repository);
	}

	public User getUser(final String email) {
		return userCache.get(email);
	}
	
	public Collection<User> getUsers(final Set<String> emails) {
		Collection<User> rV = new ArrayList<User>();
		if (emails!=null) {
			final Iterator<String> emailsIt = emails.iterator();
			while (emailsIt.hasNext()) {
				rV.add(userCache.get(emailsIt.next()));
			}
		}
		return rV;
	}
	
	public Collection<User> getUsers() {
		return userCache.values();
	}
	
	public GenericRepository getRepository(final String name) {
		return repoCache.get(name);
	}
	
	public List<GenericRepository> getRepositories() {
		final List<GenericRepository> repositories = new ArrayList<GenericRepository>();
		
		final Iterator<Entry<String, GenericRepository>> it = repoCache.entrySet().iterator();
		while (it.hasNext()) {
			repositories.add(it.next().getValue());
		}
		
		return repositories;
	}
	
	public static CommunityCache getInstance() {
		return INSTANCE;
	}
}
