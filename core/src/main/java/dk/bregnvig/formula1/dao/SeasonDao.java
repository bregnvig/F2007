package dk.bregnvig.formula1.dao;

import dk.bregnvig.formula1.Season;

public interface SeasonDao {

	void createSeason(Season season);
	
	void deleteSeason(Season season);
	
	Season findById(Long id);
}
