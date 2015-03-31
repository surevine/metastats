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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFetchUrl() {
		return fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public long getAgeInMillis() {
		return ageInMillis;
	}

	public void setAgeInMillis(long ageInMillis) {
		this.ageInMillis = ageInMillis;
	}

	public long getByteCount() {
		return byteCount;
	}

	public void setByteCount(long byteCount) {
		this.byteCount = byteCount;
	}

	public long getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(long linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public long getNumFiles() {
		return numFiles;
	}

	public void setNumFiles(long numFiles) {
		this.numFiles = numFiles;
	}

	public long getNumCommits() {
		return numCommits;
	}

	public void setNumCommits(long numCommits) {
		this.numCommits = numCommits;
	}

	public long getNumContributors() {
		return numContributors;
	}

	public void setNumContributors(long numContributors) {
		this.numContributors = numContributors;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getCocomoCost() {
		return cocomoCost;
	}

	public void setCocomoCost(String cocomoCost) {
		this.cocomoCost = cocomoCost;
	}

	public int[][] getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(int[][] punchCard) {
		this.punchCard = punchCard;
	}

	public int[] getCommitsByWeek() {
		return commitsByWeek;
	}

	public void setCommitsByWeek(int[] commitsByWeek) {
		this.commitsByWeek = commitsByWeek;
	}

	public List<User> getTopContributors() {
		return topContributors;
	}

	public void setTopContributors(List<User> topContributors) {
		this.topContributors = topContributors;
	}

	public Map<String, URI> getLinks() {
		return links;
	}

	public void setLinks(Map<String, URI> links) {
		this.links = links;
	}

	public Map<String, Integer> getLanguages() {
		return languages;
	}

	public void setLanguages(Map<String, Integer> languages) {
		this.languages = languages;
	}

	public String[] getChildren() {
		return children;
	}

	public void setChildren(String[] children) {
		this.children = children;
	}
}
