package dk.bregnvig.formula1.wbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class WBCTest extends AbstractDaoTest {

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	public void testWBCPointsSet() {
		assertTrue(season.getWBC().getPoints().size() != 0);
	}
	
	public void testWBC() throws Exception {
		
		List<Integer> points = new ArrayList<Integer>(3);
		points.add(10);
		points.add(8);
		season.getWBC().setPoints(points);
		
    	monza.addBid(flbBid);
    	monza.addBid(mbaBid);
    	monza.addBid(ttpBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResult);
   		getEntityManager().flush();
    	
   		assertEquals(season.getWBC().getPreviousRace(), monza);
   		assertEquals(3, season.getWBC().getPreviousRaceEntries().size());
   		List<WBC.Entry> entries = season.getWBC().getPreviousRaceEntries();
   		assertEquals(10, entries.get(0).getPoints());
   		assertEquals(8, entries.get(1).getPoints());
   		assertEquals(0, entries.get(2).getPoints());
   		
   		assertEquals(flb, entries.get(0).getPlayer());
   		assertEquals(mba, entries.get(1).getPlayer());
   		assertEquals(ttp, entries.get(2).getPlayer());
   		
   		assertEquals(monza, entries.get(0).getRace());
   		assertEquals(monza, entries.get(1).getRace());
   		assertEquals(monza, entries.get(2).getRace());
	}
	
	public void testStanding() throws Exception {
		testWBC();
		
		assertEquals(3, season.getWBC().getStanding().size());
		
   		List<WBC.Entry> entries = season.getWBC().getStanding();
   		assertEquals(10, entries.get(0).getPoints());
   		assertEquals(8, entries.get(1).getPoints());
   		assertEquals(0, entries.get(2).getPoints());
   		
   		assertEquals(flb, entries.get(0).getPlayer());
   		assertEquals(mba, entries.get(1).getPlayer());
   		assertEquals(ttp, entries.get(2).getPlayer());
	}
}
