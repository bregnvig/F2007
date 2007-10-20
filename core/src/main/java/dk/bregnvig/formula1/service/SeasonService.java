package dk.bregnvig.formula1.service;

import dk.bregnvig.formula1.Season;

/**
 * Provides services not are not accessiable via the rich domain model
 * @author 4u603ctpsz9
 */
public interface SeasonService {

	/**
	 * Returns the season given by the name. Null if no season was found
	 * @param name
	 * @return
	 */
	Season findByName(String name);
}
