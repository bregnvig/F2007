$(document).on("pageshow", "#bid-players", function() {
	
	var raceId = Number(location.search.replace( "?", "" ));
	var currentRace = (raceId != 0) ? F2013.allRaces.findRace(raceId) : F2013.race;
	
	$("#bid-players #title").text(currentRace.name());
	if (F2013.bidPlayers == undefined) F2013.bidPlayers = {};
	if (F2013.bidPlayers[currentRace.id()] == undefined) {
		$.mobile.loading("show", {text: "Henter bud...", textVisible: true, textonly: false, theme: "a"});
		$.getJSON(F2013.gameHost+"ws/race/"+currentRace.id()+"?players").done(function(data, textStatus, jqXHR) {
			loadPlayers(F2013.bidPlayers[currentRace.id()] = data);
			$.mobile.loading("hide");
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else loadPlayers(F2013.bidPlayers[currentRace.id()]);
	
	function loadPlayers(bids) {
		var list = $("#bid-players #players");
		$.each(bids, function() {
			var raceParam = raceId != 0 ? "&"+raceId : "";
			var link = $("<a>").text(this.player.name).prop("href", "submitted-bid.html?"+this.player.playername+raceParam);
			if (currentRace.completed()) {
				link.append($("<span>").prop("class", "ui-li-count").text(this.points));
			}
			list.append($("<li>").append(link));
		})
		list.listview("refresh");
	}
});	
