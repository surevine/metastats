function UserTable(selector) {
	var container = $(selector), mostCommits, barLength = 120;

	/**
	 * These users must be added in descending numCommits order, currently.
	 */
	this.adduser = function(user) {
		if (!mostCommits) {
			mostCommits = user.numCommits;
		}
		
		var activityWidth = barLength / mostCommits * user.numCommits;
		
		var spans = $("<span class=\"user-name\" title=\"" +user.email +"\">"
			+user.name +"</span><span class=\"activity\" style=\"width: "
			+activityWidth +"px\"/><span class=\"noactivity\" style=\"width: "
			+ (barLength-activityWidth) +"px\"/><span class=\"commits\">" +user.numCommits +" commits</span>");
		
		container.append($('<li/>').append($('<a/>', {'href': 'user?id=' +user.email}).append(spans)));
		
		spans.tooltip({
			items: "[title]",
			content: function() {
				var element = $(this);
				if (element.is("[title]")) {
					return '<div class="tooltip">' +user.name +'<span class="score">' +user.score +'</span>'
							+'<br/><img src="/monster/?seed=' +user.email +'&size=48" width="48px" />'
							+'<br/><span class="email">' +user.email +'</span>'
							+'</div>';
				}
			}
		});
	};
}