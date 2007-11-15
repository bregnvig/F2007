package dk.bregnvig.formula1.service.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.GameDao;
import dk.bregnvig.formula1.service.GameService;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GameServiceImpl implements GameService {
	
	private GameDao dao;

	@Transactional(readOnly = false)
	public Season findByName(String name) {
		return dao.findByName(name);
	}

	public void setDao(GameDao dao) {
		this.dao = dao;
	}

}
