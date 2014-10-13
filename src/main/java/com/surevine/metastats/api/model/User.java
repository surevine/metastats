package com.surevine.metastats.api.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import com.surevine.metastats.util.DateConstants;

@Data
public class User {
	
	private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

	@NotNull
	@Size(min=1)
	private String name;

	@NotNull
	@Size(min=1)
	private String email;
	
	private long
		numCommits;
	
	private int[][] punchCard = new int[DateConstants.DAYS_IN_WEEK][DateConstants.HOURS_IN_DAY];
	
	private int[] commitsByWeek = new int[DateConstants.WEEKS_IN_YEAR];
	
	private Map<String, Integer> projects = new HashMap<String, Integer>();
	
	private Map<String, Integer> languages = new HashMap<String, Integer>();
	
	public String getScore() {
		return numCommits < 1000 ? String.valueOf(numCommits) : FORMAT.format(numCommits/1000D) +"k";
	}
	
	public int hashCode() {
		return email.hashCode();
	}
	
	public boolean equals(final Object o) {
		if (o instanceof User) {
			return getEmail().equals(((User) o).getEmail());
		}
		return false;
	}
	
	public void merge(final User user) {
		// Perhaps we should modify user to track multiple names/emails
		
		for(Entry<String, Integer> project : user.getProjects().entrySet()) {
			final Integer thisProject = projects.get(project.getKey());
			
			if(thisProject == null) {
				projects.put(project.getKey(), project.getValue());
			} else {
				projects.put(project.getKey(), project.getValue() + thisProject);
			}
		}
		
		for (int i = 0; i < punchCard.length; i++) {
			for (int j = 0; j < punchCard[i].length; j++) {
				punchCard[i][j] = punchCard[i][j] + user.getPunchCard()[i][j];
			}
		}
		
		for (int i = 0; i < commitsByWeek.length; i++) {
			commitsByWeek[i] = commitsByWeek[i] + user.getCommitsByWeek()[i];
		}
		
		if(user.getLanguages() != null) {
			for(Entry<String, Integer> language : user.getLanguages().entrySet()) {
				final Integer thisLanguage = languages.get(language.getKey());
				
				if(thisLanguage == null) {
					languages.put(language.getKey(), language.getValue());
				} else {
					languages.put(language.getKey(), language.getValue() + thisLanguage);
				}
			}
		}
	}
}
