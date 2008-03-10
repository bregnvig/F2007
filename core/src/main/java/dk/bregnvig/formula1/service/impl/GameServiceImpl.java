package dk.bregnvig.formula1.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.GameDao;
import dk.bregnvig.formula1.service.GameService;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GameServiceImpl implements GameService {
	
	private GameDao dao;
//	private Season season;

	@Transactional(readOnly = true)
	public Season findByName(String name) {
		return dao.findByName(name);
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void updatePlayer(Player player) {
		dao.updatePlayer(player);
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void createPlayer(Player player) {
		dao.persist(player);
	}

	
	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void createDriver(Driver driver) {
		dao.persist(driver);
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void updateDriver(Driver driver) {
		dao.merge(driver);
	}
	
	@Transactional(readOnly = true)
	public List<Driver> findAllDrivers() {
		return dao.findAllDrivers();
	}	

	public void setDao(GameDao dao) {
		this.dao = dao;
	}

}
