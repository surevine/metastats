package com.surevine.metastats.scm;

import java.util.List;
import java.util.Map;

import com.surevine.metastats.api.model.User;

public interface GenericRepository {

	String getName();
	
	String getReadme();
	
	String getFetchUrl();
	
	Map<String, Integer> getLanguages();
	
	long getCommitCount();
	
	long getFileCount();
	
	long getByteCount();
	
	long getLinesOfCode();
	
	List<User> getUsers();
	
	int[][] getPunchCard();
	
	int[] getCommitsByWeek();
}
