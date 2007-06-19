package dk.bregnvig.formula1;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "race_result")
public class RaceResult {

	private Long id;
	private Player player;
	
	private GridResult grid;
	private FastestLapBid fastestLap;
	private PodiumResult podium;
	private SelectedDriverBid selectedDriver;
	private FirstCrashResult firstCrash;
	private int polePositionTimeMillis;

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

	@OneToOne(cascade=CascadeType.ALL, optional=false)
	public FastestLapBid getFastestLap() {
		return fastestLap;
	}

	public void setFastestLap(FastestLapBid fastestLap) {
		this.fastestLap = fastestLap;
	}

	@OneToOne(cascade=CascadeType.ALL,optional=false)
	public FirstCrashResult getFirstCrash() {
		return firstCrash;
	}

	public void setFirstCrash(FirstCrashResult firstCrash) {
		this.firstCrash = firstCrash;
	}

	@OneToOne(cascade=CascadeType.ALL,optional=false)
	public GridResult getGrid() {
		return grid;
	}

	public void setGrid(GridResult grid) {
		this.grid = grid;
	}

	@OneToOne(cascade=CascadeType.ALL,optional=false)
	public PodiumResult getPodium() {
		return podium;
	}

	public void setPodium(PodiumResult podium) {
		this.podium = podium;
	}

	@OneToOne(cascade=CascadeType.ALL, optional=false)
	public SelectedDriverBid getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(SelectedDriverBid selectedDriver) {
		this.selectedDriver = selectedDriver;
	}


	@Column(nullable=false)
	public int getPolePositionTimeMillis() {
		return polePositionTimeMillis;
	}

	public void setPolePositionTimeMillis(int polePositionTimeInMilis) {
		this.polePositionTimeMillis = polePositionTimeInMilis;
	}
}
