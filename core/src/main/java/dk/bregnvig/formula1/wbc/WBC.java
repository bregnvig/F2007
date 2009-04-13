package dk.bregnvig.formula1.wbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Configurable;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;

@Entity
@Configurable
public class WBC {

	private Long id;
	
	private Race previousRace;
	private List<Integer> points;
	private Set<Entry> entries = new HashSet<Entry>();
	
	private Comparator<Entry> entryComparator = new Comparator<Entry>() {
		public int compare(Entry entry0, Entry entry1) {
			if (entry0.getPoints() < entry1.getPoints()) {
				return 1;
			}
			if (entry0.getPoints() > entry1.getPoints()) {
				return -1;
			}
			return 0;
		}
	};

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * The result of the race. The first is the winner
	 * @param bids
	 */
	public void raceCompleted(Race race) {
		List<Bid> bids = race.getResult();
		for (int i = 0; i < bids.size(); i++) {
			Entry entry =  new Entry();
			entry.setRace(race);
			entry.setPlayer(bids.get(i).getPlayer());
			if (i < points.size()) {
				entry.setPoints(points.get(i));
			}
			addEntry(entry);
		}
		setPreviousRace(race);
	}
	
	public void raceRolledBack(Race race) {
		entries.removeAll(getRaceEntries(race));
		setPreviousRace(null);
	}
	
	private void addEntry(Entry entry) {
		entries.add(entry);
	}
	
	/**
	 * Returns the result of the previous race. The first entry in the list is 
	 * the item with most points
	 * @return
	 */
	@Transient
	public List<Entry> getPreviousRaceEntries() {
		return getRaceEntries(getPreviousRace());
	}
	
	@Transient
	public List<Entry> getRaceEntries(Race race) {
		List<Entry> result = new ArrayList<Entry>(21);
		for (Entry entry : entries) {
			if (entry.getRace().equals(race)) {
				result.add(entry);
			}
		}
		Collections.sort(result, entryComparator); 
		return result;
	}
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="wbc_id")	
	public Set<Entry> getEntries() {
		return entries;
	}
	
	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}


	@Transient
	public List<Integer> getPoints() {
		return points;
	}

	/**
	 * Sets the points awarded to the players of the game
	 * @param points
	 */
	public void setPoints(List<Integer> points) {
		this.points = points;
	}

	@OneToOne
	public Race getPreviousRace() {
		return previousRace;
	}

	public void setPreviousRace(Race previousRace) {
		this.previousRace = previousRace;
	}
	
	@Transient
	public List<Entry> getStanding() {
		
		Map<Player, Entry> resultMap = new HashMap<Player, Entry>();
		
		for (Entry entry : entries) {
			if(resultMap.containsKey(entry.getPlayer()) == false) {
				Entry nonPersistentEntry = new Entry();
				nonPersistentEntry.setPlayer(entry.getPlayer());
				resultMap.put(entry.getPlayer(), nonPersistentEntry);
			}
			resultMap.get(entry.getPlayer()).addPoints(entry.getPoints());
		}
		List<Entry> result = new ArrayList<Entry>(resultMap.size());
		result.addAll(resultMap.values());
		Collections.sort(result, entryComparator);
		return result;
	}
	
	@Entity
	@Table(name="WBCEntry")
	public static class Entry {
		
		private Long id;
		private int points;
		private Player player;
		private Race race;

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
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

		@Column(nullable=false)
		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}
		
		void addPoints(int points) {
			this.points += points;
		}

		@ManyToOne(optional=false)
		public Race getRace() {
			return race;
		}

		public void setRace(Race race) {
			this.race = race;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			
			if (obj instanceof Entry == false) {
				return false;
			}
			
			Entry other = (Entry) obj;
			
			return new EqualsBuilder()
				.append(race, other.race)
				.append(player, other.player)
				.append(points, other.points)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(race)
				.append(player)
				.append(points)
				.toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("Race", race)
				.append("Player", player)
				.append("Points", points)
				.toString();
		}
	}
}
