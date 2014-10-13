function User(id) {
	var compare = function(a, b) {
		return a.numCommits < b.numCommits;
	};
	
	$.getJSON('api/users/id/' + id, function(data) {
		$('#name').text(data.name);
		$('#score').text(data.score);
//		$('#email').text(data.email);
		
		var projects = [];
		for (var project in data.projects) {
			projects.push({"name": project, "numCommits": data.projects[project]});
		}
		projects.sort(compare);
		
		var projectTable = new ProjectTable('#projectList');
		for (var i = 0; i<projects.length; i++) {
			projectTable.addproject(projects[i]);
		}

		/** PUNCHCARD START */
		var pdata = [];
		    
		for (var i = 0; i<data.punchCard.length; i++) {
			for (var j = 0; j<data.punchCard[i].length; j++) {
				pdata.push([i,j,data.punchCard[i][j]]);
			}
		}
		
		new PunchCard(pdata);
		/** PUNCHCARD END */
	    
	    /** COMMIT GRAPH START **/
		var cData = [];
		for (var i = 0; i<data.commitsByWeek.length; i++) {
			cData.push({"week" :i, "count": data.commitsByWeek[i] });
		}
		
		new CommitGraph(cData);
	    /** COMMIT GRAPH END **/
	});
};