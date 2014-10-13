function ProjectTable(selector) {
	var container = $(selector), mostCommits, barLength = 120;

	/**
	 * These users must be added in descending numCommits order, currently.
	 */
	this.addproject = function(project) {
		if (!mostCommits) {
			mostCommits = project.numCommits;
		}
		
		var activityWidth = barLength / mostCommits * project.numCommits;
		
		container.append($('<li/>').append($('<a/>', {'href': 'project?name='
			+project.name}).append("<span class=\"project-name\">"
					+project.name +"</span><span class=\"activity\" style=\"width: "
					+activityWidth +"px\"/><span class=\"noactivity\" style=\"width: "
					+ (barLength-activityWidth) +"px\"/><span class=\"commits\">" +project.numCommits +" commits</span>")));
		
		/*spans.tooltip({
			items: "[title]",
			content: function() {
				var element = $(this);
				if (element.is("[title]")) {
					return '<div class="tooltip">' +user.name +'<span class="score">' +user.score
							+'</span><br/><span class="email">' +user.email +'</span></div>';
				}
			}
		});*/
	};
}