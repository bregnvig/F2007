function loadRaces() {
	if (F2013.allRaces == undefined) {
		$.mobile.loading("show", {text: "Henter løb...", textVisible: true, textonly: false, theme: "a"});
		$.getJSON(F2013.gameHost+"ws/races").done(function(data, textStatus, jqXHR) {
			F2013.allRaces = new Array();
			$.each(data, function() {
				F2013.allRaces.push(new Race(this));
			});
			F2013.allRaces.findRace = findRace;
			displayRaces(F2013.allRaces);
			$.mobile.loading("hide");
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		displayRaces(F2013.allRaces);
	}
	
	function displayRaces(races) {
		var list = $("#races #races-list");
		$.each(races, function() {
			if (this.viewable()) {
				list.append($("<li>").append($("<a>").text(this.name()).prop("href", "players.html?"+this.id())));
			} else {
				list.append($("<li>").text(this.name()));
			}
		});
		list.listview("refresh");
	}
	function findRace(id) {
		return $.grep(F2013.allRaces, function(race, index) {
			return race.id() == id;
		})[0];
	}
}	
