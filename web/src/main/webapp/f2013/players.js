$(document).on("pageshow", "#bid-players", function() {
	$("#bid-players #title").text(race.name());
	if (window.bidPlayers == undefined) {
		$.mobile.loading("show", {text: "Henter bud...", textVisible: true, textonly: false, theme: "a"});
		$.getJSON(gameHost+"ws/race?players").done(function(data, textStatus, jqXHR) {
			loadPlayers(window.bidPlayers = data);
			$.mobile.loading("hide");
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else loadPlayers(window.bidPlayers);
	
	function loadPlayers(players) {
		var list = $("#bid-players #players");
		$.each(players, function() {
			list.append($("<li>").append($("<a>").text(this.name).prop("href", "submitted-bid.html?"+this.playername)));
		})
		list.listview("refresh");
	}
});	
