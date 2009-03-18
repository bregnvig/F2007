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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.bid.ResultStrategy;
import dk.bregnvig.formula1.service.EventService;

/**
 * Contains a single race
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
	private Calendar open;
	private Calendar close;
	private boolean completed;
	private Driver selectedDriver;
	private Set<Bid> bids = new HashSet<Bid>();
	private Set<Player> playersThatSubmitted = new HashSet<Player>();
	private RaceResult raceResult;
	private ResultStrategy resultStrategy;
	private EventService eventService;
	
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
		validateDates(this.open, close);
		this.close = close;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getOpen() {
		return open;
	}

	public void setOpen(Calendar open) {
		validateDates(open, this.close);
		this.open = open;
	}
	
	/**
	 * Returns true if the game is opened and not closed yet
	 * @return
	 */
	@Transient
	public boolean isOpened() {
		Calendar now = Calendar.getInstance();
		return open.before(now) && close.after(now);
	}

	/**
	 * Returns true if the game has been opened and now is closed
	 * @return
	 */
	@Transient
	public boolean isClosed() {
		Calendar now = Calendar.getInstance();
		return close.before(now);
	}
	
	private void validateDates(Calendar open, Calendar close) {
		if (open == null || close == null) {
			return;
		}
		if (open.after(close)) {
			throw new IllegalStateException("Race " + name + " opens after it closes");
		}
	}
	
	private void validateOpen() {
		if (isOpened() == false) {
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
	
	/**
	 * Returns true if this player is a participant in this race
	 * @param player
	 * @return
	 */
	@Transient
	public boolean isParticipant(Player player) {
		if (isWaiting() == true) {
			return false;
		}
		if (playersThatSubmitted.isEmpty()) {
			for (Bid bid : bids) {
				playersThatSubmitted.add(bid.getPlayer());
			}
		}
		return playersThatSubmitted.contains(player);
	}

	@OneToMany(cascade=CascadeType.ALL)
	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}
	
	/**
	 * Adds a single bid to this race
	 * @param bid
	 */
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void addBid(Bid bid) {
		validateOpen();
		if (season.getPlayers().contains(bid.getPlayer()) == false) {
			throw new IllegalStateException(bid.getPlayer() + " is not part of this season");
		}
		if (isParticipant(bid.getPlayer())) {
			throw new IllegalStateException(bid.getPlayer() + " has already submitted a bid");
		}
		bid.getPlayer().getAccount().participate(this);
		bids.add(bid);
		bid.setRace(this);
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


	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public RaceResult getRaceResult() {
		return raceResult;
	}

	public void setRaceResult(RaceResult raceResult) {
		this.raceResult = raceResult;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void completeRace(RaceResult raceResult) {
		if (raceResult != null) {
			validateClosed();
			this.raceResult = raceResult;
	 		setCompleted(true);
	 		resultStrategy.calculateResult(raceResult, bids);
	 		transferWinnings();
	 		eventService.raceCompleted(this);
		}
	}
	
	private void transferWinnings() {
		List<Bid> result = getResult();
		List<Bid> winners = new ArrayList<Bid>(3);
		int winningPoints = result.get(0).getPoints(); 
		for (Bid bid : result) {
			if (bid.getPoints() == winningPoints) {
				winners.add(bid);
			} else {
				break;
			}
		}
		for (Bid winner : winners) {
			winner.getPlayer().getAccount().winnings(this, winners.size());
		}
	}
	
	/**
	 * If the race has been completed
	 * @return
	 */
	public boolean isCompleted() {
		return completed;
	}
	
	/**
	 * Returns true if the race has not yet been opened
	 * @return
	 */
	@Transient
	public boolean isWaiting() {
		return Calendar.getInstance().before(open);
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

	@Transient
	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
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
