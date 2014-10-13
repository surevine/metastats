package com.surevine.metastats.api.model;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import com.surevine.metastats.scm.GenericRepository;

@Data
public class Project {

	@NotNull
	@Size(min=1)
	private String name;
	
	private String
		fetchUrl,
		readme;
	
	@NotNull
	@Size(min=7)
	private URI uri;
	
	private long
		ageInMillis,
		byteCount,
		linesOfCode,
		numFiles,
		numCommits,
		numContributors;
	
	private String
		primaryLanguage,
		cocomoCost;
	
	private int[][] punchCard;
	
	private int[] commitsByWeek;
	
	private List<User> topContributors;
	
	private Map<String, URI> links;
	
	private Map<String, Integer> languages;
	
	private String[] children;
	
	public static Project from(final GenericRepository repository) {
		final Project p = new Project();
		p.setName(repository.getName());
		p.setNumCommits(repository.getCommitCount());
		p.setNumFiles(repository.getFileCount());
		p.setReadme(repository.getReadme());
		p.setFetchUrl(repository.getFetchUrl());
		p.setLanguages(repository.getLanguages());
		p.setByteCount(repository.getByteCount());
		p.setLinesOfCode(repository.getLinesOfCode());
		p.setPunchCard(repository.getPunchCard());
		p.setCommitsByWeek(repository.getCommitsByWeek());
		p.setCocomoCost(String.valueOf(Math.round(Math.pow(p.getLinesOfCode()/1000, 1.05d) * 2.4 / 12 * 55000)));
		
		int languageCount = 0;
		for (final String language : p.getLanguages().keySet()) {
			if (p.getPrimaryLanguage() == null || (p.getLanguages().get(language) > languageCount)
					&& !Arrays.asList("Gettext Catalog", "YAML").contains(language)) {
				languageCount = p.getLanguages().get(language);
				p.setPrimaryLanguage(language);
			}
		}
		
		final List<User> users = repository.getUsers();
		p.setNumContributors(users.size());
		
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(final User o1, final User o2) {
				return (int) (o2.getNumCommits() - o1.getNumCommits()); // Blah blah Integer.MAX_VALUE blah.
			}
		});
		
		p.setTopContributors(users.subList(0, Math.min(10, users.size())));
		
		return p;
	}
}
