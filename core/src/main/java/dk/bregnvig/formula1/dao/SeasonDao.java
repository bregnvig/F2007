package dk.bregnvig.formula1.dao;

import dk.bregnvig.formula1.Season;

public interface SeasonDao {

	Season findByName(String name);
}
