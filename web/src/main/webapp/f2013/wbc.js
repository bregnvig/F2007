$(document).on("pageshow", "#wbc", function() {
	if (window.wbcPlayers == undefined) {
		$.mobile.loading("show", {text: "Henter WBC...", textVisible: true, textonly: false, theme: "a"});
		$.getJSON(gameHost+"ws/wbc").done(function(data, textStatus, jqXHR) {
			loadPlayers(window.wbcPlayers = data);
			$.mobile.loading("hide");
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	} else loadPlayers(window.wbcPlayers);
	
	function loadPlayers(players) {
		var list = $("#wbc #players");
		$.each(players, function() {
			list.append($("<li>")
				    .append($("<a>").text(this.player.name).prop("href", "wbc-player.html?"+this.player.playername).append($("<span>").text(this.points).addClass("ui-li-aside"))
				    ));
			})
		list.listview("refresh");
	}
});	
