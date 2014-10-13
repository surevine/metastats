function ActiveProjects() {
	var projectTable = new ProjectTable('#activeProjects');
	$.getJSON('api/projects/active', function(data) {
		for (var i = 0; i<data.length; i++) {
			projectTable.addproject(data[i]);
		}
	});
}