package dk.bregnvig.formula1.bid;

import java.beans.PropertyDescriptor;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.RaceResult;

public class DefaultResultStrategy implements ResultStrategy {

	private int[] defaultPoints = {2,1};
	private int fastestPoints = 2;
	private int[] threePoints = {3,2,1};
	
	
	public void calculateResult(RaceResult raceResult, Set<Bid> bids) {
		if (bids != null) {
			for (Bid bid : bids) {
				calculatePoints(defaultPoints, raceResult.getGrid(), bid.getGrid(), 6, "position");
				calculatePoints(fastestPoints, raceResult.getFastestLap(), bid.getFastestLap(), "driver");
				calculatePoints(defaultPoints, raceResult.getPodium(), bid.getPodium(), 3, "position");
				calculatePoints(threePoints, raceResult.getFirstCrash(), bid.getFirstCrash(), 1, "crash");
				calculateSelectedDriverPoints(raceResult.getSelectedDriver(), bid.getSelectedDriver());
			}
		}
	}
	
	private void calculatePoints(int points, AbstractBid raceResult, AbstractBid bid, String propertyName) {
		if (getDriver(bid, propertyName).equals(getDriver(raceResult, propertyName))) {
			setPoints(bid, propertyName, points);
		}
	}
	
	private void calculateSelectedDriverPoints(SelectedDriverBid raceResult, SelectedDriverBid bid) {
		int index = Math.abs(raceResult.getStartPosition()-bid.getStartPosition());
		if (index < threePoints.length) {
			bid.setPointsStartPosition(threePoints[index]);
		}
		index = Math.abs(raceResult.getEndPosition()-bid.getEndPosition());
		if (index < threePoints.length) {
			bid.setPointsEndPosition(threePoints[index]);
		}
	}
	
	private void calculatePoints(int[] points, AbstractBid raceResult, AbstractBid bid, int length, String propertyName) {
		for (int i = 1; i <= length; i++) {
			Driver bidDriver = getDriver(bid, propertyName+i);
			for (int j = 0; j < points.length; j++) {
				if (i-j >= 1 && bidDriver.equals(getDriver(raceResult, propertyName+(i-j)))) {
					setPoints(bid, propertyName+(i), points[j]);
				}
				if (j != 0 && bidDriver.equals(getDriver(raceResult, propertyName+(i+j)))) {
					setPoints(bid, propertyName+(i), points[j]);
				}
			}
		}
	}
	
	private void setPoints(AbstractBid bid, String propertyName, int points) {
		propertyName = "points" + propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
		PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bid.getClass(), propertyName);
		try {
			descriptor.getWriteMethod().invoke(bid, new Object[] {points});
		} catch (Exception e) {
			throw new IllegalStateException("Could not set point for property " + propertyName);
		}
	}

	private Driver getDriver(AbstractBid bid, String propertyName) {
		PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bid.getClass(), propertyName);
		try {
			return (Driver) descriptor.getReadMethod().invoke(bid, (Object[]) null);
		} catch (Exception e) {
			throw new IllegalStateException("Could not access driver using beanutils");
		}		
	}
	
}
