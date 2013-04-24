$(document).on("pageshow", "#races", function() {
	if (window.allRaces == undefined) {
		$.mobile.loading("show", {text: "Henter løb...", textVisible: true, textonly: false, theme: "a"});
		$.getJSON(gameHost+"ws/races").done(function(data, textStatus, jqXHR) {
			loadRaces(window.allRaces = data);
			$.mobile.loading("hide");
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		loadRaces(window.allRaces);
	}
	
	function loadRaces(races) {
		var list = $("#races #races-list");
		$.each(races, function() {
			list.append($("<li>").append($("<a>").text(this.name).prop("href", "race.html?"+this.id)));
			})
		list.listview("refresh");
	}
});	
