package dk.bregnvig.formula1;

import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class RaceTest extends AbstractDaoTest {
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testRaceCreated() {
		assertNotNull(monza.getId());
		assertNotNull(monza.getSeason());
	}

	public void testRaceFind() {
		assertNotNull(getEntityManager().find(Race.class, monza.getId()));
	}
	
	public void testDeleteRace() {
		getEntityManager().remove(monza);
		assertNull(getEntityManager().find(Race.class, monza.getId()));
	}
	
	public void testAddBid() {
		Bid bid = helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 100000);
		monza.addBid(bid);
		getEntityManager().flush();
		assertNotNull(bid.getId());
	}

	public void testAddBidForNonPlayer() {
		
		Player player = new Player();
		player.setFirstName("Fie");
		player.setLastName("Pedersen");
		player.setPlayername("fie");
		getEntityManager().persist(player);
		
		assertNotNull(player.getId());
		
		Bid bid = helper.getBid(player, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 100000);
		try {
			monza.addBid(bid);
			fail("Player not part of the game");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void addBidToNonOpenGame() {
		try {
			spa.addBid(helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 100000));
			fail("Game is not open");
		} catch (IllegalStateException expected) {
			
		}
	}

	public void addBidToClosedGame() {
		try {
			Race australia = new Race();
			australia.setSelectedDriver(massa);
			australia.setName("Spa");
			Calendar begin = Calendar.getInstance();
			begin.add(Calendar.MINUTE, -7);
			australia.setBegin(begin);
			Calendar close2 = Calendar.getInstance();
			close2.add(Calendar.MINUTE, -5);
			australia.setClose(close2);
			season.addRace(australia);
			australia.addBid(helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 100000));
			fail("Game is not open");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void testDoubleAddBid() {
		Bid bid = helper.getBid(flb, massa, kimi, hamilton, alonso, heidfeld, kubica, 1, 1, 100000);
		monza.addBid(bid);
		getEntityManager().flush();
		assertNotNull(bid.getId());
		try {
			bid = helper.getBid(flb, massa, kimi, hamilton, heidfeld, alonso, kubica, 1, 1, 100000);
			monza.addBid(bid);
			fail("Double bid from same player should fail");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	
	
	public void testValidDates() {
		monza.setBegin(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		try {
			monza.setClose(close);
		} catch (IllegalStateException e) {
			fail("Dates should have been ok");
		}
	}
	
	public void testInvalidDates() {
		monza.setBegin(Calendar.getInstance());
		monza.getBegin().add(Calendar.MONTH, 1);
		Calendar close = Calendar.getInstance();
		try {
			monza.setClose(close);
			fail("Dates should have failed ok");
		} catch (IllegalStateException expected) {
		}
	}
	
    public void testRaceConfiguration() {
        Race race = new Race();
        assertNotNull("Result strategy should have been provided", race.getResultStrategy());
    }
    
    public void testRaceResultNonClosedGame() {
    	
    	try {
    		monza.setRaceResult(raceResult);
    	} catch (IllegalStateException expected) {
    	}
    }

    public void testRaceResultGame() throws Exception {
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResult);
   		getEntityManager().flush();
   		assertNotNull(raceResult.getId());
    }

    public void testResult() throws Exception{
    	monza.addBid(flbBid);
    	monza.addBid(mbaBid);
    	monza.addBid(ttpBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResult);
   		getEntityManager().flush();
    	
   		assertNotNull(flbBid.getId());
   		assertNotNull(mbaBid.getId());
   		assertNotNull(ttpBid.getId());
 		assertNotNull(raceResult.getId());
 		
 		List<Bid> result = monza.getResult();
 		assertEquals(3, result.size());
 		assertEquals(flbBid, result.get(0));
 		assertEquals(mbaBid, result.get(1));
 		assertEquals(ttpBid, result.get(2));
    }

    public void testResultBetterPole() throws Exception{
    	monza.addBid(flbBid);
    	mbaBid = helper.getBid(mba, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 11000);
		
    	monza.addBid(mbaBid);
    	monza.addBid(ttpBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResult);
   		getEntityManager().flush();
    	
   		assertNotNull(flbBid.getId());
   		assertNotNull(mbaBid.getId());
   		assertNotNull(ttpBid.getId());
 		assertNotNull(raceResult.getId());
 		
 		List<Bid> result = monza.getResult();
 		assertEquals(3, result.size());
 		assertEquals(flbBid, result.get(0));
 		assertEquals(mbaBid, result.get(1));
 		assertEquals(ttpBid, result.get(2));
    }
}
