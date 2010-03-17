package dk.bregnvig.formula1.wbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.DummySMSGatewayImpl;;

public class WBCTest extends AbstractDaoTest {
	
	private SMSCongratulator congratulator;

	public void setCongratulator(SMSCongratulator congratulator) {
		this.congratulator = congratulator;
	}

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
   		monza.completeRace(raceResultMonza);
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
	
	private void create2RaceWBC() throws Exception {
		testWBC();
		
		spa.setOpen(Calendar.getInstance());
		Thread.sleep(100);
		
		spa.addBid(flbSpaBid);
		spa.addBid(mbaSpaBid);
		spa.addBid(ttpSpaBid);
		
		Thread.sleep(200);
		spa.setClose(Calendar.getInstance());
		Thread.sleep(200);
		spa.completeRace(raceResultSpa);
		getEntityManager().flush();
		
   		assertEquals(season.getWBC().getPreviousRace(), spa);
	}
	
	public void testStanding() throws Exception {
		
		create2RaceWBC();
    	
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
	
	public void testHistory() throws Exception {
		create2RaceWBC();
		
		assertEquals(2, season.getWBC().getHistory().size());
		
		assertEquals(3, season.getWBC().getHistory().get(0).getPositions().size());
		assertEquals(3, season.getWBC().getHistory().get(1).getPositions().size());

		List<WBC.PlayerPosition> positions = season.getWBC().getHistory().get(0).getPositions();
		
		assertEquals(10, positions.get(0).getPoints());
		assertEquals(8, positions.get(1).getPoints());
		assertEquals(6, positions.get(2).getPoints());
		assertEquals(1, positions.get(0).getPosition());
		assertEquals(2, positions.get(1).getPosition());
		assertEquals(3, positions.get(2).getPosition());
		
		positions = season.getWBC().getHistory().get(1).getPositions();
		
		assertEquals(20, positions.get(0).getPoints());
		assertEquals(16, positions.get(1).getPoints());
		assertEquals(12, positions.get(2).getPoints());
		assertEquals(1, positions.get(0).getPosition());
		assertEquals(2, positions.get(1).getPosition());
		assertEquals(3, positions.get(2).getPosition());
	}
	
	public void testPlayerEntries() throws Exception {
		create2RaceWBC();
   		
   		List<WBC.Entry> flbEntries = season.getWBC().getPlayerEntries(flb);
   		List<WBC.Entry> mbaEntries = season.getWBC().getPlayerEntries(mba);
   		
   		assertEquals(2, flbEntries.size());
   		assertEquals(2, mbaEntries.size());
   		
   		assertEquals(flb, flbEntries.get(0).getPlayer());
   		assertEquals(flb, flbEntries.get(1).getPlayer());
   		assertEquals(mba, mbaEntries.get(0).getPlayer());
   		assertEquals(mba, mbaEntries.get(1).getPlayer());
   		
   		assertEquals(monza, flbEntries.get(0).getRace());
   		assertEquals(spa, flbEntries.get(1).getRace());
   		assertEquals(monza, mbaEntries.get(0).getRace());
   		assertEquals(spa, mbaEntries.get(1).getRace());
   		
   		assertEquals(10, flbEntries.get(0).getPoints());
   		assertEquals(10, flbEntries.get(1).getPoints());
   		assertEquals(8, mbaEntries.get(0).getPoints());
   		assertEquals(8, mbaEntries.get(1).getPoints());
	}
	
	public void testRollback() throws Exception {
		testStanding();
		
		season.getWBC().raceRolledBack(spa);
		
		List<WBC.Entry> entries = season.getWBC().getStanding();
   		assertEquals(10, entries.get(0).getPoints());
   		assertEquals(8, entries.get(1).getPoints());
   		assertEquals(6, entries.get(2).getPoints());
   		
   		assertEquals(flb, entries.get(0).getPlayer());
   		assertEquals(mba, entries.get(1).getPlayer());
   		assertEquals(ttp, entries.get(2).getPlayer());
   		
   		assertEquals(3, season.getWBC().getRaceEntries(monza).size());
	}
	
	public void testSMSSending() throws Exception {

		List<Integer> points = new ArrayList<Integer>(3);
		points.add(10);
		points.add(8);
		points.add(6);
		season.getWBC().setPoints(points);
		
		DummySMSGatewayImpl service = new DummySMSGatewayImpl();
		congratulator.setSMSGateway(service);
		
    	monza.addBid(flbMonzaBid);
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.completeRace(raceResultMonza);
   		getEntityManager().flush();
   		
   		assertTrue(service.containsTheseValues("flb", Integer.toString(10)));
   		assertTrue(service.containsTheseValues("mba", Integer.toString(8)));
   		assertTrue(service.containsTheseValues("ttp", Integer.toString(6)));
		
	}
}
