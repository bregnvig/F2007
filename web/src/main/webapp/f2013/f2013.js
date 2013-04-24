$.fn.doesExist = function(){
    return jQuery(this).length > 0;
};

var user = new User();
var race = new Race();
var drivers = null;
var bid = new Bid();
var error = "";
var forceReload = false;
var testMode = window.location.search.replace( "?", "" ) == "test";
var gameHost = location.host == "m.formel1.loopit.eu" ? "http://formel1.loopit.eu/" : "../";

$(document).on('pageinit', function(event) {
	$.ajaxSetup({
		beforeSend: function(jqXHR){
			if (user.isValid()) jqXHR.setRequestHeader("Authorization", user.authorizationValue());
		},
		cache: false
	});
	if (Modernizr.localstorage == false) {
		$.mobile.changePage($("#no-support-local-storage"));
		return;
	} else if ('withCredentials' in new XMLHttpRequest()) == false) {
		$.mobile.changePage($("#no-support-xhr"));
		return;
	} else {
		$(document).on("pageshow", "#home", function(event, ui) {
			if (user.isValid() == false) {
				setTimeout(function() {
					$.mobile.changePage("login.html", {transition: "slidedown"});
				}, 500);
				//
			} 
			if (forceReload || ui.prevPage.length == 0) loadHome();
		});
	}
	switch (event.target.id) {
	case "home":
		$.ajax({url: gameHost + "ws/season-name", crossDomain: true, type: "GET", dataType: 'text'}).done(function(data) {
			$('#title').text(data);
			document.title = data;
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
		$(document).on("swipeleft", "#home", function(event) {
			$.mobile.changePage("wbc.html");
		})
		$(document).on("swiperight", "#wbc", function(event) {
			history.back();
		})
		break;
	}
});

function loadHome() {
	forceReload = false;
	$("#participate").parent().hide();
	$("#players").parent().hide();

	if (user.isValid()) {
		$.mobile.loading("show", {text: "Henter løbet...", textVisible: true, textonly: false, theme: "a"});
		$.ajax({url: gameHost+'ws/race', dataType: 'json'}).done(function(data, textStatus, jqXHR) {
			race = new Race(data);
			$("#race-name").text(race.name());
			$("#race-status").text(race.status());
			if (race.open()) {
				$("#participate").parent().show();
				$.ajax({url: gameHost+"ws/race/drivers", dataType: 'json'}).done(function(data) {
					drivers = new Drivers(data);
					$.mobile.loading("hide");
				}).fail(function(jqxhr, textStatus, error) {
					gotoErrorPage(error);
				});
			} else {
				if (race.isParticipant()) {
					$("#players").parent().show();
				}
				$.mobile.loading("hide");
			}
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else {
		$("#race-status").text("Du er ikke logget ind");
	}
}

$("#not-working").on("pageshow", function(event, ui) {
	$("#error-text").text(error);
});

function gotoErrorPage(error) {
	$.mobile.loading("hide");
	this.error = error;
	$.mobile.changePage($("#not-working"));
}

