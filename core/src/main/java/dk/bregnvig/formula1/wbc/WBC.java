package dk.bregnvig.formula1.wbc;

import java.util.ArrayList;
import java.util.Collection;
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
	
	private Comparator<Entry> entryPointsComparator = new Comparator<Entry>() {
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

	private Comparator<Entry> entryRaceDateComparator = new Comparator<Entry>() {
		public int compare(Entry entry0, Entry entry1) {
			return entry0.race.getOpen().compareTo(entry1.race.getOpen());
		}
	};

	private Comparator<PlayerPosition> historyPointsComparator = new Comparator<PlayerPosition>() {
		public int compare(PlayerPosition entry0, PlayerPosition entry1) {
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
		Collections.sort(result, entryPointsComparator); 
		return result;
	}
	
	@Transient
	public List<Entry> getPlayerEntries(Player player) {
		List<Entry> result = new ArrayList<Entry>(21);
		for (Entry entry : entries) {
			if (entry.getPlayer().equals(player)) {
				result.add(entry);
			}
		}
		Collections.sort(result, entryRaceDateComparator); 
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
		Collections.sort(result, entryPointsComparator);
		return result;
	}
	
	@Transient
	public List<History> getHistory() {
		List<History> result = new ArrayList<History>();
		List<Entry> entries = new ArrayList<Entry>(getEntries());
		Collections.sort(entries, entryRaceDateComparator);
		
		Map<Player, PlayerPosition> players = new HashMap<Player, PlayerPosition>();
		
		Race previousRace = null;
		for (Entry entry : entries) {
			if (previousRace == null || previousRace.equals(entry.getRace()) == false) {
				if (previousRace != null) {
					result.add(getHistory(previousRace, players.values()));
				}
				previousRace = entry.getRace();
			}
			//Do the add
			if (players.containsKey(entry.getPlayer()) == false) {
				players.put(entry.getPlayer(), new PlayerPosition(entry.player));
			}
			PlayerPosition position = players.get(entry.getPlayer());
			position.addPoints(entry.getPoints());
		}
		if (previousRace != null) {
			result.add(getHistory(previousRace, players.values()));
		}
		return result;
	}
	
	private History getHistory(Race race, Collection<PlayerPosition> positions) {
		if (positions == null || positions.isEmpty()) return null;
		List<PlayerPosition> sorted = new ArrayList<PlayerPosition>(positions);
		Collections.sort(sorted, historyPointsComparator);
		int lastPoint = 0;
		int wbcPosition = 1;

		History history = new History(race);
		for (int i = 0; i < sorted.size(); i++) {
			PlayerPosition position = new PlayerPosition(sorted.get(i));
			if (i == 0) {
				lastPoint = position.getPoints();
			}
			if (lastPoint == position.getPoints()) {
				position.setPosition(wbcPosition);
			} else {
				lastPoint = position.getPoints();
				position.setPosition(++wbcPosition);
			}
			history.addPosition(position);
		}
		return history; 
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
	
	public static class PlayerPosition {
		
		private int position;
		private Player player;
		private int points;
		
		public PlayerPosition() {
			
		}
		
		public PlayerPosition(Player player) {
			this.player = player;
		}
		
		public PlayerPosition(PlayerPosition source) {
			this(source.player);
			this.points = source.points;
			this.position = source.position;
		}
		
		public void addPoints(int points) {
			this.points += points; 
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	
		public Player getPlayer() {
			return player;
		}
	
		public void setPlayer(Player player) {
			this.player = player;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PlayerPosition other = (PlayerPosition) obj;
			if (player == null) {
				if (other.player != null)
					return false;
			} else if (!player.equals(other.player))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((player == null) ? 0 : player.hashCode());
			return result;
		}
		
		
	}
	
	public static class History {
		private Race race;
		private List<PlayerPosition> positions = new ArrayList<PlayerPosition>();
		
		public History(Race race) {
			this.race = race;
		}
		
		void addPosition(PlayerPosition position) {
			positions.add(position);
		}
		
		public Race getRace() {
			return race;
		}
		public void setRace(Race race) {
			this.race = race;
		}
		public List<PlayerPosition> getPositions() {
			return positions;
		}
		public void setPositions(List<PlayerPosition> positions) {
			this.positions = positions;
		}
	}
}
