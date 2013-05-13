var User = function() {
	
	this._token = localStorage["token"];
	this._playername = localStorage["playername"];
	this._name = localStorage["name"];
	
	this.isValid = function() {
		return this.token() != null;
	},
	this.playername = function(playername) {
		if (playername === undefined) return this._playername == null ? "" : this._playername;
		this._playername = playername;
		if (this.rememberMe()) localStorage["playername"] = playername;
	},
	this.token = function(token) {
		if (token === undefined) return this._token;
		this._token = token;
		if (this.rememberMe()) localStorage["token"] = token;
	},
	this.name = function(name) {
		if (name === undefined) return this._name;
		this._name = name;
		if (this.rememberMe())localStorage["name"] = name;
	},
	this.rememberMe = function(rememberMe) {
		if (rememberMe === undefined) return localStorage["remember-me"]  == null || localStorage["remember-me"] == "true";
		localStorage["remember-me"] = rememberMe;
	},
	this.reset = function() {
		localStorage.removeItem("playername");
		localStorage.removeItem("token");
		localStorage.removeItem("name");
	},
	this.authorizationValue = function() {
		if (this.isValid() == false) return null;
		return "Basic " + window.btoa(this.playername()+":"+this.token());
	}
}

var Race = function(data) {
	this.json = data;
	if (data === undefined) {
		try {
			this.json = JSON.parse(localStorage["race"]);
		} catch (err) {
			this.json = null;
		}
	} else {
		localStorage["race"] = JSON.stringify(data);
	}
	this.id = function() {
		return this.json.id;
	}
	this.name = function() {
		return this.json.name;
	}
	this.selectedDriver = function() {
		return this.json.selectedDriver.name;
	}
	this.open = function() {
		return this.json.opened == true && this.json.participant == false; 
	}
	this.completed = function() {
		return this.json.completed == true; 
	}
	this.viewable = function() {
		return this.json.participant || this.json.closed == true;
	}
	this.status = function() {
		if (this.json.participant == true) return "Du har allerede spillet";
		if (this.json.closed == true && this.json.completed == false) {
			return "Der kan ikke længere afgives bud";
		}
		if (this.json.opened == true) return "Spillet er åbent for bud";
		if (this.json.opened == false && this.json.completed == false) {
			return "Spillet er ikke åbent endnu";
		} 
		return "Ukendt status";
	}
	this.isParticipant = function() {
		return this.json.participant;
	}
}

var Driver = function(data) {
	
	this.json = data;
	
	this.isValid = function() {
		return this.json != null;
	}
	
	this.name = function() {
		return this.json.name;
	}
	this.id = function() {
		return this.json.id;
	}
	this.active = function() {
		return this.json.active;
	}
	this.option = function() {
		return $("<option>").prop("value", this.json.id).text(this.json.name);
	}
}

var Drivers = function(data) {
	
	this.drivers = [];
	
	var drivers = this.drivers;
	$.each(data, function(i, driver){
		F2013.drivers.push(new Driver(driver));
	});
	
	this.populateWithDrivers = function(selectElement) {
		$.each(this.drivers, function(i, driver) {
			selectElement.append(driver.option());
		});
		selectElement.selectmenu("refresh");
	}
	this.populateWithPositions = function(selectElement) {
		$.each(this.drivers, function(i, driver) {
			selectElement.append($("<option>").prop("value", i+1).text(i+1));
		});
		selectElement.selectmenu("refresh");
	}
	this.getDriver = function(id) {
		return $.grep(this.drivers, function(driver) {
			return driver.id() == id;
		})[0];
	}
}

var Bid = function() {
	this.polePositionTime = 0;
	this.podium = [];
	this.grid = [];
	this.fastestLap = null; 
	this.firstCrash = null; 
	this.selectedDriver = [];
}