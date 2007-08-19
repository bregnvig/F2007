package dk.bregnvig.formula1.wbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.event.AbstractRaceListener;
import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.SMSServiceDummyImpl;

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
		points.add(6);
		season.getWBC().setPoints(points);
		
    	monza.addBid(flbMonzaBid);
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResultMonza);
   		getEntityManager().flush();
    	
   		assertEquals(season.getWBC().getPreviousRace(), monza);
   		assertEquals(3, season.getWBC().getPreviousRaceEntries().size());
   		List<WBC.Entry> entries = season.getWBC().getPreviousRaceEntries();
   		assertEquals(10, entries.get(0).getPoints());
   		assertEquals(8, entries.get(1).getPoints());
   		assertEquals(6, entries.get(2).getPoints());
   		
   		assertEquals(flb, entries.get(0).getPlayer());
   		assertEquals(mba, entries.get(1).getPlayer());
   		assertEquals(ttp, entries.get(2).getPlayer());
   		
   		assertEquals(monza, entries.get(0).getRace());
   		assertEquals(monza, entries.get(1).getRace());
   		assertEquals(monza, entries.get(2).getRace());
	}
	
	public void testStanding() throws Exception {
		testWBC();

		spa.setOpen(Calendar.getInstance());
    	Thread.sleep(100);
		
    	spa.addBid(flbSpaBid);
    	spa.addBid(mbaSpaBid);
    	spa.addBid(ttpSpaBid);
    	
    	Thread.sleep(200);
    	spa.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		spa.setRaceResult(raceResultSpa);
   		getEntityManager().flush();
    	
   		assertEquals(season.getWBC().getPreviousRace(), spa);
   		assertEquals(6, season.getWBC().getEntries().size());
		
		assertEquals(3, season.getWBC().getStanding().size());
		
   		List<WBC.Entry> entries = season.getWBC().getStanding();
   		assertEquals(20, entries.get(0).getPoints());
   		assertEquals(16, entries.get(1).getPoints());
   		assertEquals(12, entries.get(2).getPoints());
   		
   		assertEquals(flb, entries.get(0).getPlayer());
   		assertEquals(mba, entries.get(1).getPlayer());
   		assertEquals(ttp, entries.get(2).getPlayer());
	}
	
	public void testSMSSending() throws Exception {

		List<AbstractRaceListener> listeners =  monza.getListeners();
		
		SMSCongratulator congratulator = null;
		for (AbstractRaceListener listener : listeners) {
			if (listener instanceof SMSCongratulator) {
				congratulator = (SMSCongratulator) listener;
			}
		}
		
		SMSServiceDummyImpl service = new SMSServiceDummyImpl();
		congratulator.setSMSService(service);
		
    	monza.addBid(flbMonzaBid);
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResultMonza);
   		getEntityManager().flush();
   		
   		assertTrue(service.containsTheseValues("flb", Integer.toString(10)));
   		assertTrue(service.containsTheseValues("mba", Integer.toString(8)));
   		assertTrue(service.containsTheseValues("ttp", Integer.toString(6)));
		
	}
}
