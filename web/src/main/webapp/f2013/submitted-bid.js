$(document).on("pageshow", "#submitted-bid", function() {
	
	$.mobile.loading("show", {text: "Henter bud...", textVisible: true, textonly: false, theme: "a"});
	var page = $("#submitted-bid"); 
	var userId = location.search.replace( "?", "" );
	
	if (window.bids == undefined) window.bids = {};
	
	if (window.bids[userId] == undefined) {
		$.getJSON(gameHost+"ws/race/"+race.json.id+"/bid/"+userId)
		.done(function(data, textStatus, jqXHR) {
			displayBid(window.bids[userId] = data);
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		displayBid(window.bids[userId]);
	}
	
	function displayBid(data) {
		page.find("#title").text(data.player.name);
		jQuery.each(data.grid, function() {
			page.find("#fastest-lap").before($("<li>").text(this.name));
		});
		page.find("#fastest-lap").after($("<li>").text(data.fastestLap.name));
		jQuery.each(data.podium, function() {
			page.find("#selected-driver").before($("<li>").text(this.name));
		});
		page.find("#selected-driver").after($("<li>").text("Slutter som nr.: " + data.selectedDriver[1]));
		page.find("#selected-driver").after($("<li>").text("Starter som nr.: " + data.selectedDriver[0]));
		page.find("#first-crash").after($("<li>").text(data.firstCrash.name));
		page.find("#pole-position-time").after($("<li>").text(data.polePositionTimeInText));
		page.find("#bid").listview("refresh");
		$.mobile.loading("hide");
	}
});	
