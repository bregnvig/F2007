package dk.bregnvig.formula1.dao.impl;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.dao.SeasonDao;

public class SeasonJpaDaoImpl extends JpaDaoSupport implements SeasonDao {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public Season findByName(String name) {
		List<Season> seasons = getJpaTemplate().findByNamedQuery("season.findByName", name);
		
		if (seasons == null || seasons.size() == 0) {
			return null;
		}
		return seasons.get(0);
	}

}
