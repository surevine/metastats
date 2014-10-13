function ActiveUsers() {
	var userTable = new UserTable('#activeUsers');
	$.getJSON('api/users/active', function(data) {
		for (var i = 0; i<data.length; i++) {
			userTable.adduser(data[i]);
		}
	});
}