package dk.bregnvig.formula1.service.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.SeasonDao;
import dk.bregnvig.formula1.service.SeasonService;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class SeasonServiceImpl implements SeasonService {
	
	private SeasonDao dao;

	@Transactional(readOnly = false)
	public Season findByName(String name) {
		return dao.findByName(name);
	}

	public void setDao(SeasonDao dao) {
		this.dao = dao;
	}

}
