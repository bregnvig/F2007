package dk.bregnvig.formula1;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
	private Race race;
	
	private GridBid grid;
	private FastestLapBid fastestLap;
	private PodiumBid podium;
	private SelectedDriverBid selectedDriver;
	private FirstCrashBid firstCrash;
	private int polePositionTimeMillis;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional=false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public FastestLapBid getFastestLap() {
		return fastestLap;
	}

	public void setFastestLap(FastestLapBid fastestLap) {
		this.fastestLap = fastestLap;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public FirstCrashBid getFirstCrash() {
		return firstCrash;
	}

	public void setFirstCrash(FirstCrashBid firstCrash) {
		this.firstCrash = firstCrash;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public GridBid getGrid() {
		return grid;
	}

	public void setGrid(GridBid grid) {
		this.grid = grid;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public PodiumBid getPodium() {
		return podium;
	}

	public void setPodium(PodiumBid podium) {
		this.podium = podium;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public SelectedDriverBid getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(SelectedDriverBid selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	@Transient
	public int getPoints() {
		if (race != null && race.isCompleted() == false) {
			throw new IllegalStateException("Race not completed. Hence points not available");
		}
		return grid.getPoints()+fastestLap.getPoints()+podium.getPoints()+selectedDriver.getPoints()+firstCrash.getPoints();
	}

	@Column(nullable=false)
	public int getPolePositionTimeMillis() {
		return polePositionTimeMillis;
	}

	public void setPolePositionTimeMillis(int polePositionTimeInMilis) {
		this.polePositionTimeMillis = polePositionTimeInMilis;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Bid == false) {
			return false;
		}
		
		Bid other = (Bid) obj;
		
		return new EqualsBuilder()
			.append(player, other.player)
			.append(grid, other.grid)
			.append(fastestLap, other.fastestLap)
			.append(podium, other.podium)
			.append(selectedDriver, other.selectedDriver)
			.append(firstCrash, other.firstCrash)
			.append(polePositionTimeMillis, other.polePositionTimeMillis)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(player)
			.append(grid)
			.append(fastestLap)
			.append(podium)
			.append(selectedDriver)
			.append(firstCrash)
			.append(polePositionTimeMillis)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append(player)
			.append(grid)
			.append(fastestLap)
			.append(podium)
			.append(selectedDriver)
			.append(firstCrash)
			.append("Pole position time", polePositionTimeMillis)
			.toString();
	}

	@ManyToOne()
	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

}
