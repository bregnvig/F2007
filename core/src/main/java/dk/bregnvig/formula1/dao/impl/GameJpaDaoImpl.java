package dk.bregnvig.formula1.dao.impl;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.GameDao;

public class GameJpaDaoImpl extends JpaDaoSupport implements GameDao {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Season findByName(String name) {
		List<Season> seasons = getJpaTemplate().findByNamedQuery("season.findByName", name);
		
		if (isEmpty(seasons)) {
			return null;
		}
		return seasons.get(0);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Player findPlayerByName(String playerName) {
		List<Player> players = getJpaTemplate().findByNamedQuery("player.findByName", playerName);
		
		if (isEmpty(players)) {
			return null;
		}
		return players.get(0);
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void updatePlayer(Player player) {
		getJpaTemplate().merge(player);
	}

	
	@SuppressWarnings("unchecked")
	public List<Driver> findAllDrivers() {
		List<Driver> drivers = getJpaTemplate().findByNamedQuery("driver.findAll");
		return drivers;
	}

	@SuppressWarnings("unchecked")
	public List<Player> findAllPlayers() {
		List<Player> players = getJpaTemplate().findByNamedQuery("player.findAll");
		return players;
	}
	
	private boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}

	public <T> T merge(T object) {
		return getJpaTemplate().merge(object);
	}

	public void persist(Object object) {
		getJpaTemplate().persist(object);
	}
	
	public void flush() {
		getJpaTemplate().flush();
	}
}
