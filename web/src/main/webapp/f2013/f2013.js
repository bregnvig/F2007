$.fn.doesExist = function(){
    return jQuery(this).length > 0;
};

var F2013 = {};

F2013.seasonname = "";
F2013.user = new User();
F2013.race = new Race();
F2013.drivers = null;
F2013.bid = new Bid();
F2013.error = "";
F2013.forceReload = false;
F2013.testMode = window.location.search.replace( "?", "" ) == "test";
F2013.gameHost = location.host == "m.formel1.loopit.eu" ? "http://formel1.loopit.eu/" : "../";

$(document).on('pageinit', function(event) {
	$.ajaxSetup({
		beforeSend: function(jqXHR){
			if (F2013.user.isValid()) jqXHR.setRequestHeader("Authorization", F2013.user.authorizationValue());
		},
		cache: false
	});
	if (Modernizr.localstorage == false) {
		$.mobile.changePage($("#no-support-local-storage"));
		return;
	} else if ('withCredentials' in new XMLHttpRequest() == false) {
		$.mobile.changePage($("#no-support-xhr"));
		return;
	} else {
		$(document).on("pageshow", "#home", function(event, ui) {
			if (F2013.user.isValid() == false) {
				setTimeout(function() {
					$.mobile.changePage("login.html", {transition: "slidedown"});
				}, 500);
				//
			} 
			if (F2013.forceReload || ui.prevPage.length == 0) loadHome();
		});
	}
	switch (event.target.id) {
	case "home":
		$.ajax({url: F2013.gameHost + "ws/season-name", crossDomain: true, type: "GET", dataType: 'text'}).done(function(data) {
			$('#title').text(F2013.seasonname = data);
			document.title = data;
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
		break;
	}
});

function loadHome() {
	F2013.forceReload = false;
	$("#participate").parent().hide();
	$("#players").parent().hide();

	if (F2013.user.isValid()) {
		$.mobile.loading("show", {text: "Henter løbet...", textVisible: true, textonly: false, theme: "a"});
		$.ajax({url: F2013.gameHost+'ws/race', dataType: 'json'}).done(function(data, textStatus, jqXHR) {
			race = new Race(data);
			$("#race-name").text(F2013.race.name());
			$("#race-status").text(F2013.race.status());
			if (F2013.race.open()) {
				$("#participate").parent().show();
				$.ajax({url: F2013.gameHost+"ws/race/drivers", dataType: 'json'}).done(function(data) {
					drivers = new Drivers(data);
					$.mobile.loading("hide");
				}).fail(function(jqxhr, textStatus, error) {
					gotoErrorPage(error);
				});
			} else {
				if (F2013.race.viewable()) {
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
	$("#error-text").text(F2013.error);
});

function gotoErrorPage(error) {
	$.mobile.loading("hide");
	F2013.error = error;
	$.mobile.changePage($("#not-working"));
}

function pageSwiped(swiper) {
	if (swiper.previousIndex != swiper.activeIndex) {
		$(".dot").removeClass("active");
		$("#dot"+swiper.activeIndex).addClass("active");
		switch (swiper.activeIndex) {
		case 0:
			$('#title').text('Alle løb');
			loadRaces();
			break;
		case 1:
			$('#title').text(F2013.seasonname);
			break;
		case 2:
			$('#title').text('WBC');
			loadWBC();
			break;
		}
	}
}

