$(document).on("pageshow", "#submitted-bid", function() {
	
	$.mobile.loading("show", {text: "Henter bud...", textVisible: true, textonly: false, theme: "a"});
	var page = $("#submitted-bid"); 
	var userId = location.search.replace( "?", "" ).split("&")[0];
	var raceId = location.search.replace( "?", "" ).split("&")[1];
	if (raceId == undefined) raceId = F2013.race.json.id;
	
	if (F2013.bids == undefined) F2013.bids = {};
	
	if (F2013.bids[userId] == undefined) {
		$.getJSON(F2013.gameHost+"ws/race/"+raceId+"/bid/"+userId)
		.done(function(data, textStatus, jqXHR) {
			displayBid(F2013.bids[userId] = data);
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		displayBid(F2013.bids[userId]);
	}
	
	function displayBid(data) {
		page.find("#title").text(data.player.name);
		jQuery.each(data.grid, function() {
			page.find("#fastest-lap").before(createLi(this));
		});
		page.find("#fastest-lap").after(createLi(data.fastestLap));
		jQuery.each(data.podium, function() {
			page.find("#selected-driver").before(createLi(this));
		});
		page.find("#selected-driver").after(createSelectedDriverLi("Slutter som nr.: ", data.selectedDriver[1], data.selectedDriverPoints[1]));
		page.find("#selected-driver").after(createSelectedDriverLi("Starter som nr.: ", data.selectedDriver[0], data.selectedDriverPoints[0]));
		page.find("#first-crash").after(createLi(data.firstCrash));
		page.find("#pole-position-time").after($("<li>").text(data.polePositionTimeInText));
		page.find("#bid").listview("refresh");
		$.mobile.loading("hide");
		
		function createLi(driver) {
			li = $("<li>").text(driver.name);
			if (data.points != 0) {
				li.append($("<span>").prop("class", "ui-li-count").text(driver.points));
			}
			return li;
		}
		function createSelectedDriverLi(text, position, points) {
			li = $("<li>").text(text + position);
			if (data.points != 0) {
				li.append($("<span>").prop("class", "ui-li-count").text(points));
			}
			return li;
		}
	}
	
});	
