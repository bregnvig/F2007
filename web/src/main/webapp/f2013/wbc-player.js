$(document).on("pageshow", "#wbc-player", function() {
	
	$.mobile.loading("show", {text: "Henter WBC...", textVisible: true, textonly: false, theme: "a"});
	var page = $("#wbc-player"); 
	var userId = location.search.replace( "?", "" );
	
	if (window.wbcPlayer == undefined) window.wbcPlayer = {};
	
	if (window.wbcPlayer[userId] == undefined) {
		$.getJSON(F2013.gameHost+"ws/wbc/"+userId)
		.done(function(data, textStatus, jqXHR) {
			displayWBC(window.wbcPlayer[userId] = data);
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		displayWBC(window.wbcPlayer[userId]);
	}
	
	function displayWBC(data) {
		if (data == null || data.length == 0) return;
		page.find("#title").text(data[0].player.name);
		var list = page.find("#wbc");
		$.each(data, function() {
			list.append($("<li>").text(this.race.name).append($("<span>").text(this.points).addClass("ui-li-aside")));
//				    .append($("<span>").text(this.points).addClass("ui-li-aside"))
//				    ));
		});
		page.find("#wbc").listview("refresh");
		$.mobile.loading("hide");
	}
});	
