package dk.bregnvig.formula1;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dk.bregnvig.formula1.bid.FastestLapBid;
import dk.bregnvig.formula1.bid.FirstCrashResult;
import dk.bregnvig.formula1.bid.GridResult;
import dk.bregnvig.formula1.bid.PodiumResult;
import dk.bregnvig.formula1.bid.SelectedDriverBid;

@Entity
@Table(name = "bid")
public class RaceResult {

	private Long id;
	private Player player;
	
	private GridResult grid;
	private FastestLapBid fastestLap;
	private PodiumResult podium;
	private SelectedDriverBid selectedDriver;
	private FirstCrashResult firstCrash;

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
	public FirstCrashResult getFirstCrash() {
		return firstCrash;
	}

	public void setFirstCrash(FirstCrashResult firstCrash) {
		this.firstCrash = firstCrash;
	}

	@OneToOne(optional=false)
	public GridResult getGrid() {
		return grid;
	}

	public void setGrid(GridResult grid) {
		this.grid = grid;
	}

	@OneToOne(optional=false)
	public PodiumResult getPodium() {
		return podium;
	}

	public void setPodium(PodiumResult podium) {
		this.podium = podium;
	}

	@OneToOne(optional=false)
	public SelectedDriverBid getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(SelectedDriverBid selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

}
