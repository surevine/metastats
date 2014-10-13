function Project(name) {
	var compareLanguages = function(a, b) {
		return b.files - a.files;
	};
	
	$.getJSON('api/projects/named/' + name, function(data) {
		$('#readme').text(data.readme ? data.readme : "No readme found.");
		$('#commits').text(data.numCommits ? data.numCommits : -1);
		$('#files').text(data.numFiles ? data.numFiles : -1);
		$('#linesOfCode').text(data.linesOfCode ? data.linesOfCode : -1);
		$('#contributors').text(data.numContributors ? data.numContributors : -1);
		$('#cocomo').text(data.cocomoCost ? "$" +data.cocomoCost : "Unknown");

		var userTable = new UserTable('#topContributorList');
		for (var i = 0; i<data.topContributors.length; i++) {
			userTable.adduser(data.topContributors[i]);
		}

		if (data.fetchUrl) {
			$('#fetchurl').append(
					$('<a href="' + data.fetchUrl + '">' + data.fetchUrl
							+ '</a>'));
		}
		
		/** LANGUAGES START */
		var width = 420, height = 420, radius = Math.min(width, height) / 2;

		var color = d3.scale.ordinal().range(
				[ "#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56",
						"#d0743c", "#ff8c00" ]);

		var arc = d3.svg.arc().outerRadius(radius - 10)
				.innerRadius(radius - 120);

		var pie = d3.layout.pie().sort(null).value(function(d) {
			return d.files;
		});

		var svg = d3.select("#languages").append("svg").attr("width", width)
				.attr("height", height).append("g").attr("transform",
						"translate(" + width / 2 + "," + height / 2 + ")");

		var pieData = [];
		for (var language in data.languages) {
			pieData.push({"name": language, "files": data.languages[language]});
		}
		
		pieData.sort(compareLanguages);

		pieData.forEach(function(d) {
			d.files = +d.files;
		});

		var g = svg.selectAll(".arc").data(pie(pieData)).enter().append("g").attr(
				"class", "arc");

		g.append("path").attr("d", arc).style("fill", function(d) {
			return color(d.data.name);
		});

		g.append("text").attr("transform", function(d) {
			return "translate(" + arc.centroid(d) + ")";
		}).attr("dy", ".35em").style("text-anchor", "middle").text(function(d) {
			return d.data.name;
		});
		/** LANUGAGES END */
		
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
}