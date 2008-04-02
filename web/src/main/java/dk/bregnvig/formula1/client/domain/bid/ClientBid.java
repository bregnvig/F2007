package dk.bregnvig.formula1.client.domain.bid;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;

public class ClientBid implements IsSerializable {

	private ClientPlayer player;
	private ClientDriver[] grid;
	private ClientDriver fastestLap;
	private ClientDriver firstCrash;
	private ClientDriver[] podium;
	private int[] selectedDriver;
	private int[] selectedDriverPoints = {0,0};
	private int polePositionTime;
	
	private int points;

	public ClientPlayer getPlayer() {
		return player;
	}

	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}

	public ClientDriver[] getGrid() {
		return grid;
	}

	public void setGrid(ClientDriver[] grid) {
		this.grid = grid;
	}

	public ClientDriver getFastestLap() {
		return fastestLap;
	}

	public void setFastestLap(ClientDriver fastestLap) {
		this.fastestLap = fastestLap;
	}

	public ClientDriver[] getPodium() {
		return podium;
	}

	public void setPodium(ClientDriver[] podium) {
		this.podium = podium;
	}

	public int[] getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(int[] selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	public int getPolePositionTime() {
		return polePositionTime;
	}

	public void setPolePositionTime(int polePositionTime) {
		this.polePositionTime = polePositionTime;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public ClientDriver getFirstCrash() {
		return firstCrash;
	}

	public void setFirstCrash(ClientDriver firstCrash) {
		this.firstCrash = firstCrash;
	}

	public int[] getSelectedDriverPoints() {
		return selectedDriverPoints;
	}

	public void setSelectedDriverPoints(int[] selectedDriverPoints) {
		this.selectedDriverPoints = selectedDriverPoints;
	}

	public String getPolePositionTimeInText() {
		NumberFormat secondFormatter = NumberFormat.getFormat("00");
		NumberFormat milliSecondFormatter = NumberFormat.getFormat("000");
		
		String result = Integer.toString((int) getPolePositionTime() / (1000*60));
		result += ":" + secondFormatter.format(((int)getPolePositionTime() % (1000*60)) / 1000);
		result += "." + milliSecondFormatter.format(getPolePositionTime() % 1000); 
		return result;
	}
	
}
