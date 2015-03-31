package com.surevine.metastats.scm.git;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.FS;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.CommitDiffEditFilter;

import com.google.common.io.Files;
import com.surevine.metastats.api.model.User;
import com.surevine.metastats.cache.CommunityCache;
import com.surevine.metastats.scm.GenericRepository;
import com.surevine.metastats.util.DateConstants;
import com.surevine.metastats.util.FileTraversal;
import com.surevine.metastats.util.Language;

public class GitRepository implements GenericRepository {
	
	private static final long
		THIRTY_DAYS_MS = 1000L * 60L * 60L * 24L * 7L * 52L;
	
	private static final String[] CANDIDATE_README = new String[] {
			"README",
			"README.txt",
			"readme.txt",
			"readme.md",
			"README.md"
	};

	private String
			fetchUrl,
			name,
			readme;
	
	private long
		codeFileCount = 0,
		commitCount = 0,
		fileCount = 0,
		byteCount = 0,
		linesOfCode = 0,
		thirtyDaysAgo;
	
	private Set<String> emails;
	
	private Map<String, Integer> languages = new HashMap<String, Integer>();
	
	private final Map<String, User> emailToUser = new HashMap<String, User>();

	private int[][] punchCard = new int[DateConstants.DAYS_IN_WEEK][DateConstants.HOURS_IN_DAY];
	
	private int[] commitsByWeek = new int[DateConstants.WEEKS_IN_YEAR];
	
	public GitRepository(final File root) {
		thirtyDaysAgo = System.currentTimeMillis() - THIRTY_DAYS_MS;
		
		name = root.getName();
		
		readFilesystem(root);
		
		walkCommitLog(root);
		
		workOutUserLanguages(root);
		
		removeLanguagesBelowFivePercent();
	}
	
	private void workOutUserLanguages(File root) {
		UserLanguageFilter filter = new UserLanguageFilter();

		try {
			Repository repository = RepositoryCache.open(
					RepositoryCache.FileKey.lenient(root, FS.DETECTED), true);
			
			CommitFinder finder = new CommitFinder(repository);
			finder.setFilter(filter).findFrom("master");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void walkCommitLog(final File root) {
		
		try {
			Repository repository = RepositoryCache.open(
					RepositoryCache.FileKey.lenient(root, FS.DETECTED), true);
			
			final Git git = new Git(repository);
			
//			if (ConfigurationProperties.is(ConfigurationProperties.REPOSITORY_PULL)) {
//				git.pull().call();
//			}
			
			fetchUrl = repository.getConfig().getString("remote", "origin", "url");
			
			/** Get last commit */
//				 RevCommit c = null;
//				 AnyObjectId headId;
//				 try {
//				     headId = git.resolve(Constants.HEAD);
//				     RevCommit root = repository.parseCommit(headId);
//				     repository.sort(RevSort.REVERSE);
//				     repository.markStart(root);
//				     c = repository.next();
//				 } catch (IOException e) {
//				     e.printStackTrace();
//				 }
			/** Get last commit */
			
			for (final RevCommit commit : git.log().call()) {
				commitCount++;
				
				// Populate a user and store against email.
				final User user;
				if (!emailToUser.containsKey(commit.getAuthorIdent().getEmailAddress())) {
					user = new User();
					user.setName(commit.getAuthorIdent().getName());
					user.setEmail(commit.getAuthorIdent().getEmailAddress());
					user.setNumCommits(1L);
					emailToUser.put(user.getEmail(), user);
				} else {
					user = emailToUser.get(commit.getAuthorIdent().getEmailAddress());
					user.setNumCommits(user.getNumCommits() +1L);
				}
				
				// Populate projects on User.
				final Map<String, Integer> projects;
				if (user.getProjects() == null) {
					projects = new HashMap<String, Integer>();
				} else {
					projects = user.getProjects();
				}
				if (!projects.containsKey(getName())) {
					projects.put(getName(), 1);
				} else {
					projects.put(getName(), projects.get(getName()).intValue() +1);
				}
				
				user.setProjects(projects);
				
				if (commit.getAuthorIdent().getWhen().getTime() > thirtyDaysAgo) {
					handleRecentCommit(user, commit);
				}
			}
			
			emails = emailToUser.keySet();
			
			CommunityCache.getInstance().addUsers(emailToUser.values());
		} catch (IOException e) {
			System.out.println(name);
			e.printStackTrace();
		} catch (NoHeadException e) {
			System.out.println(name);
			e.printStackTrace();
		} catch (GitAPIException e) {
			System.out.println(name);
			e.printStackTrace();
		}
	}
	
	private void handleRecentCommit(final User user, final RevCommit commit) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(commit.getAuthorIdent().getWhen());
		
		final int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		final int hours = calendar.get(Calendar.HOUR_OF_DAY);
		final int week = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
		
		punchCard[day][hours] = punchCard[day][hours] +1;
		commitsByWeek[week] = commitsByWeek[week] +1;
		
		user.getPunchCard()[day][hours] = user.getPunchCard()[day][hours] +1;
		user.getCommitsByWeek()[week] = user.getCommitsByWeek()[week] +1;
	}
	
	private class UserLanguageFilter extends CommitDiffEditFilter {
		protected boolean include(RevCommit commit, DiffEntry diff, Edit hunk) {
			try {
			switch (hunk.getType()) {
				case INSERT:
					// Look up user
					User user = emailToUser.get(commit.getAuthorIdent().getEmailAddress());
					
					int lastSep = diff.getNewPath().lastIndexOf(File.separator);
					
					String filename;
					
					if(lastSep > -1) {
						filename = diff.getNewPath().substring(lastSep + 1);
					} else {
						filename = diff.getNewPath();
					}
					
					String language = Language.get(filename);
					
					if(language != null) {
						
						Integer count = user.getLanguages().get(language);
						
						if(count == null) {
							user.getLanguages().put(language, hunk.getLengthB());
						} else {
							user.getLanguages().put(language, hunk.getLengthB() + count.intValue());
						}
					} else {
						// ignore
					}
					break;
				case REPLACE:
	//				edited += hunk.getLengthB();
					// Should we take edits into account?
					break;
				default:
					break;
			}
			
			} catch(Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}
/*	
	private void parseUserLanguagesFromCommit(RevWalk walker, RevCommit commit, User user) {
		final ObjectReader reader = walker.getObjectReader();
		
		Collection<DiffEntry> diffs = commit.get
		
		for (DiffEntry diff : diffs) {
			if (!isFileDiff(diff))
				continue;
			final AbbreviatedObjectId oldId = diff.getOldId();
			if (oldId == null)
				continue;
			
			Collection<Edit> edits = BlobUtils.diff(reader,
					oldId.toObjectId(), diff.getNewId().toObjectId());
			
			int lines = 0;
			
			for(Edit edit : edits) {
				switch (edit.getType()) {
				case INSERT:
					lines += edit.getLengthB();
					break;
				case REPLACE:
//					edited += hunk.getLengthB();
					// Do we record edits?
					break;
				default:
					break;
				}
			}
		}
	}
	
	private boolean isFileDiff(DiffEntry diff) {
		switch (diff.getChangeType()) {
		case DELETE:
			return TYPE_FILE == (diff.getOldMode().getBits() & TYPE_MASK);
		case ADD:
			return TYPE_FILE == (diff.getNewMode().getBits() & TYPE_MASK);
		default:
			return TYPE_FILE == (diff.getOldMode().getBits() & TYPE_MASK)
					&& TYPE_FILE == (diff.getNewMode().getBits() & TYPE_MASK);
		}
	}
	*/
	private void readFilesystem(final File root) {
		try {
			new FileTraversal() {
				public void onFile(final File f) {
					fileCount++;
					
					byteCount += f.length();
					
					String language = Language.get(f.getName());
					if (language != null) {
						codeFileCount++;
						int fileLines = countLines(f);
						linesOfCode += fileLines;
						
						if (languages.containsKey(language)) {
							languages.put(language, languages.get(language) +fileLines);
						} else {
							languages.put(language, fileLines);
						}
					}
				}
				
				public boolean onDirectory(final File d) {
					return !d.getName().equals(".git");
				}
			}.traverse(root);
			
			for (final String readme : CANDIDATE_README) {
				final File readmeFile = new File(root, readme);
				if (readmeFile.exists()) {
					setReadme(Files.toString(readmeFile, Charset.defaultCharset()));
					break;
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	private void removeLanguagesBelowFivePercent() {
		int other = 0;
		
		final List<String> toRemove = new ArrayList<String>();
		
		for (final String lKey : languages.keySet()) {
			// If less than 5% of the project is one language we bung it in other.
			// This saves unreadable slices on a pie chart.
			if (languages.get(lKey) < (linesOfCode / 20)) {
				other += languages.get(lKey);
				toRemove.add(lKey);
			}
		}
		
		for (final String remove : toRemove) {
			languages.remove(remove);
		}
		
		if (other > 0) {
			languages.put("Other", other);
		}
	}
	
	private int countLines(final File target) {
		try {
			final InputStream is = new BufferedInputStream(new FileInputStream(target));
		    try {
		        byte[] c = new byte[1024];
		        int count = 0;
		        int readChars = 0;
		        boolean empty = true;
		        while ((readChars = is.read(c)) != -1) {
		            empty = false;
		            for (int i = 0; i < readChars; ++i) {
		                if (c[i] == '\n')
		                    ++count;
		            }
		        }
		        
		        return (count == 0 && !empty) ? 1 : count;
		    } finally {
		        is.close();
		    }
		} catch (final IOException ioe) {
			ioe.printStackTrace();
			return 0;
		}
	}
	@Override
	public List<User> getUsers() {
		return new ArrayList<User>(CommunityCache.getInstance().getUsers(emails));
	}

	public String getFetchUrl() {
		return fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	public long getCodeFileCount() {
		return codeFileCount;
	}

	public void setCodeFileCount(long codeFileCount) {
		this.codeFileCount = codeFileCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(long commitCount) {
		this.commitCount = commitCount;
	}

	public long getFileCount() {
		return fileCount;
	}

	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
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

	public long getThirtyDaysAgo() {
		return thirtyDaysAgo;
	}

	public void setThirtyDaysAgo(long thirtyDaysAgo) {
		this.thirtyDaysAgo = thirtyDaysAgo;
	}

	public Set<String> getEmails() {
		return emails;
	}

	public void setEmails(Set<String> emails) {
		this.emails = emails;
	}

	public Map<String, Integer> getLanguages() {
		return languages;
	}

	public void setLanguages(Map<String, Integer> languages) {
		this.languages = languages;
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

	public Map<String, User> getEmailToUser() {
		return emailToUser;
	}
}
