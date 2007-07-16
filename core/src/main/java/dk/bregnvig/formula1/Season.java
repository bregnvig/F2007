package dk.bregnvig.formula1;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.wbc.WBC;

/**
 * This class is a entire season of formula 1
 * @author bregnvig
 *
 */
@Entity
@Table(name="season")
@Transactional(propagation=Propagation.REQUIRED)
public class Season {
	
	private Long id;
	
	private String name;
	private WBC wbc = new WBC();
	private Set<Race> races = new HashSet<Race>(21);
	private Set<Driver> drivers = new HashSet<Driver>(25);
	private Set<Player> players = new HashSet<Player>(17);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=50, nullable=false, unique=true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a single player to the season. The player must already be persisted
	 * @param player
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	@ManyToMany(fetch=FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="season_player")
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public void addRace(Race race) {
		race.setSeason(this);
		races.add(race);
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="season")
	public Set<Race> getRaces() {
		return races;
	}
	public void setRaces(Set<Race> races) {
		this.races = races;
	}
	
	public void addDriver(Driver driver) {
		drivers.add(driver);
	}
	
	public void removeDriver(Driver driver) {
		drivers.remove(driver);
	}

	@ManyToMany(fetch=FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name="season_driver")
	public Set<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(Set<Driver> drivers) {
		this.drivers = drivers;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public WBC getWBC() {
		return wbc;
	}

	public void setWBC(WBC wbc) {
		this.wbc = wbc;
	}
}
