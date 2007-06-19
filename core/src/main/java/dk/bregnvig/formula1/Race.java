package dk.bregnvig.formula1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Configurable;

import dk.bregnvig.formula1.bid.ResultStrategy;

/**
 * Contains a single
 * 
 * @author bregnvig
 * 
 */
@Entity
@Table(name="race")
@Configurable
public class Race {

	private Long id;

	private Season season;
	private String name;
	private Calendar begin;
	private Calendar close;
	private boolean completed;
	private Driver selectedDriver;
	private Set<Bid> bids = new HashSet<Bid>();
	private Set<Player> playersThatSubmitted = new HashSet<Player>();
	private RaceResult raceResult;
	private ResultStrategy resultStrategy;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=25, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getClose() {
		return close;
	}

	public void setClose(Calendar close) {
		validateDates(this.begin, close);
		this.close = close;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar open) {
		validateDates(open, this.close);
		this.begin = open;
		
	}
	
	/**
	 * Returns true if the game is open§
	 * @return
	 */
	@Transient
	public boolean isOpen() {
		Calendar now = Calendar.getInstance();
		return begin.before(now) && close.after(now);
	}

	/**
	 * Returns true if the game has been opened and no is closed
	 * @return
	 */
	@Transient
	public boolean isClosed() {
		Calendar now = Calendar.getInstance();
		return close.before(now);
	}
	
	private void validateDates(Calendar begin, Calendar close) {
		if (begin == null || close == null) {
			return;
		}
		if (begin.after(close)) {
			throw new IllegalStateException("Race " + name + " starts after is ends");
		}
	}
	
	private void validateOpen() {
		if (isOpen() == false) {
			throw new IllegalStateException("Race " + name + " is not open");
		}
	}
	
	private void validateClosed() {
		if (isClosed() == false) {
			throw new IllegalStateException("Race " + name + " has not been closed");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Race == false) {
			return false;
		}
		
		Race other = (Race) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(name, other.name)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "Race: " + name;
	}

	@ManyToOne(optional=false, cascade={CascadeType.REFRESH, CascadeType.MERGE})
	public Driver getSelectedDriver() {
		return selectedDriver;
	}

	public void setSelectedDriver(Driver selectedDriver) {
		this.selectedDriver = selectedDriver;
	}

	@ManyToOne(optional=false, cascade={CascadeType.REFRESH, CascadeType.MERGE})
	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}
	
	@Transient
	public Set<Driver> getDrivers() {
		
		Set<Driver> result = new HashSet<Driver>();
		
		if (season.getDrivers() != null) {
			for (Driver driver : season.getDrivers()) {
				if (driver.isActive()) {
					result.add(driver);
				}
			}
		}
		return result;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="race_id", nullable=false)
	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
		for (Bid bid : bids) {
			playersThatSubmitted.add(bid.getPlayer());
		}
	}
	
	/**
	 * Adds a single bid to this race
	 * @param bid
	 */
	public void addBid(Bid bid) {
		validateOpen();
		if (season.getPlayers().contains(bid.getPlayer()) == false) {
			throw new IllegalStateException(bid.getPlayer() + " is not part of this season");
		}
		if (playersThatSubmitted.contains(bid.getPlayer())) {
			throw new IllegalStateException(bid.getPlayer() + " has already submitted a bid");
		}
		bids.add(bid);
		playersThatSubmitted.add(bid.getPlayer());
	}
	
	/**
	 * Returns the result of the race in order from best to worst
	 * @return
	 */
	@Transient
	public List<Bid> getResult() {
		validateCompleted();
		Comparator<Bid> comparator = new ResultComparator(raceResult);
		List<Bid> result = new ArrayList<Bid>(bids);
		Collections.sort(result, Collections.reverseOrder(comparator));
		return result;
	}


	@OneToOne(cascade = CascadeType.ALL)
	public RaceResult getRaceResult() {
		return raceResult;
	}

	public void setRaceResult(RaceResult raceResult) {
		if (raceResult != null) {
			validateClosed();
			this.raceResult = raceResult;
	 		setCompleted(true);
	 		resultStrategy.calculateResult(raceResult, bids);
		}
	}

	public boolean isCompleted() {
		return completed;
	}

	private void validateCompleted() {
		if (isCompleted() == false) {
			throw new IllegalStateException("The race is not complete");
		}
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Transient
	public ResultStrategy getResultStrategy() {
		return resultStrategy;
	}

	public void setResultStrategy(ResultStrategy resultStrategy) {
		this.resultStrategy = resultStrategy;
	}
	
	private class ResultComparator implements Comparator<Bid> {
		
		private RaceResult raceResult;
		
		public ResultComparator(RaceResult raceResult) {
			this.raceResult = raceResult;
		}

		public int compare(Bid bid1, Bid bid2) {
			if (bid1.getPoints() < bid2.getPoints()) {
				return -1;
			}
			if (bid1.getPoints() > bid2.getPoints()) {
				return 1;
			}
			int off1 = Math.abs(raceResult.getPolePositionTimeMillis() - bid1.getPolePositionTimeMillis()); 
			int off2 = Math.abs(raceResult.getPolePositionTimeMillis() - bid2.getPolePositionTimeMillis());
			if (off1 > off2) {
				return -1;
			}
			if (off1 < off2) {
				return 1;
			}
			return 0;
		}
	}
}
