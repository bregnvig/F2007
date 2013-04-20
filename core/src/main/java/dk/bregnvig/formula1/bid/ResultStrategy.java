package dk.bregnvig.formula1.bid;

import java.util.Set;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.RaceResult;

/**
 * Implents how to calculta the result of a race
 * @author 4u603ctpsz9
 *
 */
public interface ResultStrategy {

	/**
	 * Calculates the points for every bid. The point awarded are directly written to the bids
	 * @param raceResult
	 * @param bids
	 */
	void calculateResult(RaceResult raceResult, Set<Bid> bids);
}
