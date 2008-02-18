package dk.bregnvig.formula1.service.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.GameDao;
import dk.bregnvig.formula1.service.GameService;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GameServiceImpl implements GameService {
	
	private GameDao dao;
	private Season season;

	@Transactional(readOnly = true)
	public Season findByName(String name) {
		return season = dao.findByName(name);
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void updatePlayer(Player player) {
		dao.updatePlayer(player);
	}

	public void setDao(GameDao dao) {
		this.dao = dao;
	}

	@Transactional(readOnly = false, propagation=Propagation.REQUIRED)
	public void addBid(Bid bid) {
		// TODO Auto-generated method stub
		season.getCurrentRace().addBid(bid);
		dao.merge(season);
	}

}
