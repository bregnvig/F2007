package dk.bregnvig.formula1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dk.bregnvig.formula1.bid.FastestLapBid;
import dk.bregnvig.formula1.bid.FirstCrashBid;
import dk.bregnvig.formula1.bid.GridBid;
import dk.bregnvig.formula1.bid.PodiumBid;
import dk.bregnvig.formula1.bid.SelectedDriverBid;

@Entity
@Table(name = "bid")
public class Bid {

	private Long id;
	private Player player;
	
	private GridBid grid;
	private FastestLapBid fastestLap;
	private PodiumBid podium;
	private SelectedDriverBid selectedDriver;
	private FirstCrashBid firstCrash;
	private int points;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional=false)
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@OneToOne(optional=false)
	public FastestLapBid getFastestLap() {
		return fastestLap;
	}

	public void setFastestLap(FastestLapBid fastestLap) {
		this.fastestLap = fastestLap;
	}

	@OneToOne(optional=false)
	public FirstCrashBid getFirstCrash() {
		return firstCrash;
	}

	public void setFirstCrash(FirstCrashBid firstCrash) {
		this.firstCrash = firstCrash;
	}

	@OneToOne(optional=false)
	public GridBid getGrid() {
		return grid;
	}

	public void setGrid(GridBid grid) {
		this.grid = grid;
	}

	@OneToOne(optional=false)
	public PodiumBid getPodium() {
		return podium;
	}

	public void setPodium(PodiumBid podium) {
		this.podium = podium;
	}

	@OneToOne(optional=false)
	public SelectedDriverBid getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(SelectedDriverBid selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
