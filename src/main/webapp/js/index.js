$(document).ready(function() {
	$.getJSON('api/init/all', function(data) {
		$('#repositoryCount').text(data.repositoryCount);
		$('#commitCount').text(data.commitCount);
		$('#loc').text(data.linesOfCode);
		$('#contributorCount').text(data.contributorCount);
		$('#fileCount').text(data.fileCount);
	});
	
	new ActiveProjects();
	new ActiveUsers();
	
	new ProjectTree();
});