package dk.bregnvig.formula1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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
import dk.bregnvig.formula1.event.AbstractRaceListener;
import dk.bregnvig.formula1.event.RaceTimer;

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
	
	private static BigDecimal bettingAmount = new BigDecimal(20);

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
	private List<AbstractRaceListener> listeners = new ArrayList<AbstractRaceListener>();
	private List<RaceTimer> timers = new ArrayList<RaceTimer>();
	private Timer timer;

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
		createRaceListeners();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getOpen() {
		return open;
	}

	public void setOpen(Calendar open) {
		validateDates(open, this.close);
		this.open = open;
		createRaceTimers();
		createRaceListeners();
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

	@Transient
	public List<RaceTimer> getTimers() {
		return timers;
	}

	/**
	 * Sets the timers that the race has to deal with
	 * @param timers
	 */
	public void setTimers(List<RaceTimer> timers) {
		this.timers = timers;
	}

	@Transient
	public List<AbstractRaceListener> getListeners() {
		return listeners;
	}
	
	/**
	 * Sets the listeners that the race has to deal with
	 * @param listeners
	 */
	public void setListeners(List<AbstractRaceListener> listeners) {
		this.listeners = listeners;
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
		bid.getPlayer().getAccount().withdraw("Deltagelse i " + getName(), getBettingAmount());
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
	 		for (AbstractRaceListener listener : listeners) {
	 			listener.raceCompleted();
			}
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
	
	private void createRaceTimers() {
		if (timer != null) {
			timer.cancel();
		}
		
		if (isWaiting() == false || timers == null || timers.size() == 0) {
			return; 
		}
		
		timer = new Timer();
		Date now = new Date();
		for (RaceTimer raceTimer : timers) {
			raceTimer.setRace(this);
			Date when  = open.getTime();
			when.setTime(when.getTime()-raceTimer.getBeforeInMillis());
			if (when.after(now)) {
				timer.schedule(new InternalRaceTimer(raceTimer), when);
			}
		}
	}
	
	private void createRaceListeners() {
		if (open == null || close == null || isCompleted()) {
			return;
		}
		for (AbstractRaceListener listener : listeners) {
			listener.setRace(this);
		}
	}
	
	private class InternalRaceTimer extends TimerTask {
		
		private RaceTimer raceTimer;
		
		public InternalRaceTimer(RaceTimer raceTimer) {
			this.raceTimer = raceTimer;
		}

		@Override
		public void run() {
			raceTimer.invoke();
		}
		
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

	@Transient
	public static BigDecimal getBettingAmount() {
		return bettingAmount;
	}

	public static void setBettingAmount(BigDecimal bettingAmount) {
		Race.bettingAmount = bettingAmount;
	}

}
