$(document).on("pageinit", "#login", function() {
	$('#login #playername').val(user.playername());
	$('#login #remember-me').prop("checked", user.rememberMe()).checkboxradio("refresh");

	$('#login #login-btn').click(function() {
		user.playername($('#login #playername').val());

		$.ajax(gameHost+'ws/login/'+user.playername()+"/"+$('#login #password').val(), {crossDomain: true, type: "GET"}).done(function(data) {
			user.name(data.name);
			user.token(data.token);
			forceReload = true;
			history.back();
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	});
	$('#login #remember-me').click(function() {
		user.rememberMe($(this).is(':checked'));
		if(user.rememberMe() == false) {
			user.reset();
		}
	});	
});