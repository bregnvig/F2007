$(document).on("pageinit", "#bid", function() {
	
	$("#bid #title").text(race.name());
	$("#bid #selected-driver").find(".ui-btn-text").text(race.selectedDriver());
	
	drivers.populateWithDrivers($("select[id^=grid]"));
	drivers.populateWithDrivers($("select[id^=fastest]"));
	drivers.populateWithDrivers($("select[id^=podium]"));
	drivers.populateWithDrivers($("select[id^=first-crash]"));
	drivers.populateWithPositions($("select[id^=selected-driver]"));

	var seconds = $("#pole-time-seconds");
	for(var i = 0; i < 60; i++) {
		seconds.append($("<option>").prop("value", i).text(i));
	}
	var miliseconds = $("#pole-time-miliseconds");
	for(var i = 0; i < 1000; i++) {
		miliseconds.append($("<option>").prop("value", i).text(i));
	}
	new BidValidator();
	
	if (testMode) loadTest();
	
	$("#indsend").click(function(event) {
		setDrivers(bid.grid, $("select[id^=grid]"));
		setDrivers(bid.podium, $("select[id^=podium]"));
		bid.fastestLap = getDriver($("select[id^=fastest]"));
		bid.firstCrash = getDriver($("select[id^=first-crash]"));
		setPositions(bid.selectedDriver, $("select[id^=selected-driver]"));
		bid.polePositionTime = getPolePositionTime();
		$.mobile.loading("show", {text: "Indsender bud...", textVisible: true, textonly: false});
		$.ajax(gameHost+"ws/bid", {
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify(bid)
		}).done(function() {
			$.mobile.loading("hide");
			forceReload = true;
			history.back();
		}).fail(function(jqxhr, textStatus, error) {
			gotoErrorPage(error);
		});
	});
	
	function setDrivers(target, source) {
		source.each(function(i) {
			var select = $(this);
			if (select.val() == "") return true;
			target[i] = drivers.getDriver(select.val()).json;
		}); 
	}
	function getDriver(source) {
		return source.val() == "" ? null : drivers.getDriver(source.val()).json;
	}
	function setPositions(target, source) {
		source.each(function(i) {
			var select = $(this);
			if (select.val() == "") return true;
			target[i] = select.val();
		}); 
	}
	function getPolePositionTime() {
		var total = $("#pole-time-minutes").val()*60*1000;
		total += $("#pole-time-seconds").val()*1000;
		return total + parseInt($("#pole-time-miliseconds").val());
	}
	
	function loadTest() {
		$("select").each(function(i) {
			this.selectedIndex = i+1;
		});
		$("#pole-time-minutes").val(1);
		$("select").trigger("change").selectmenu("refresh");
	}
});

var BidValidator = function() {
	this.gridValid = false;
	this.fastestValid = false;
	this.podiumValid = false;
	this.selectedDriverValid = false;
	this.firstCrashValid = false;
	this.poleTimeValid = false;
	
	validator = this;
	
	$("select[id^=grid]").change(function(event) {
		validator.gridValid = validateDriverSelection($("select[id^=grid]"));
	})
	$("select[id^=fastest]").change(function(event) {
		validator.fastestValid = validateDriverSelection($("select[id^=fastest]"));
	})
	$("select[id^=podium]").change(function(event) {
		validator.podiumValid = validateDriverSelection($("select[id^=podium]"));
	})
	$("select[id^=first-crash]").change(function(event) {
		validator.firstCrashValid = validateDriverSelection($("select[id^=first-crash]"));
	})
	$("select[id^=selected-driver]").change(function(event) {
		validator.selectedDriverValid = validateRequired($("select[id^=selected-driver]"));
	})
	$("select[id^=pole-time]").change(function(event) {
		validator.poleTimeValid = validateRequired($("select[id^=pole-time]"));
	})
	
	$("select").change(function(event) {
		if (validator.isValid())$("#indsend").removeClass("ui-disabled");
		else $("#indsend").addClass("ui-disabled");
	})
	
	this.isValid = function() {
		return this.gridValid && this.fastestValid && this.podiumValid && this.selectedDriverValid && this.firstCrashValid && this.poleTimeValid;
	}
	
	function validateDriverSelection(selects) {
		var valid = true;
		selects.each(function() {
			var select = $(this);
			var newTheme = select.val() == "" ? "c" : "d"; 
			innerSelect = this;
			if (newTheme != "c") {
				selects.each(function(i, element) {
					if (element != innerSelect && element.value == innerSelect.value) {
						newTheme = "e";
						return valid = false; 
					} 
				});
			} else {
				valid = false;
			}
			changeTheme(select, newTheme);
		})
		selects.selectmenu("refresh");
		return valid;
	}
	
	function validateRequired(selects) {
		var valid = true;
		selects.each(function() {
			var select = $(this);
			var newTheme = select.val() == "" ? "c" : "d"; 
			changeTheme(select, newTheme);
			valid = newTheme != "c" && valid;
		})
		return valid;
	}

	function changeTheme(select, newTheme) {
		var previousTheme = select.parent().attr("data-theme");
		if (newTheme != previousTheme) {
			select.parent()
				.attr("data-theme", newTheme).attr("data-icon", "arrow-"+newTheme)
				.removeClass("ui-btn-up-"+previousTheme).removeClass("ui-btn-hover-"+previousTheme).removeClass("ui-btn-down-"+previousTheme)
				.addClass("ui-btn-up-"+newTheme);
		}		
	}
}
