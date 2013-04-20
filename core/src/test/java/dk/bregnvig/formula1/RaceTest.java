package dk.bregnvig.formula1;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class RaceTest extends AbstractDaoTest {
	
	public RaceTest() {
//		setDefaultRollback(false);
	}
	
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
		Bid bid = helper.getBid(flb, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 100000);
		monza.addBid(bid);
		getEntityManager().flush();
		assertNotNull(bid.getId());
	}

	public void testAddBidForNonPlayer() {
		
		Player player = new Player();
		player.setFirstName("Fie");
		player.setLastName("Pedersen");
		player.setPlayername("fie");
		player.setPassword("fie");
		getEntityManager().persist(player);
		
		assertNotNull(player.getId());
		
		Bid bid = helper.getBid(player, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 100000);
		try {
			monza.addBid(bid);
			fail("Player not part of the game");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void addBidToNonOpenGame() {
		try {
			spa.addBid(helper.getBid(flb, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 100000));
			fail("Game is not open");
		} catch (IllegalStateException expected) {
			
		}
	}

	public void addBidToClosedGame() {
		try {
			Race australia = new Race();
			australia.setSelectedDriver(driver2);
			australia.setName("Spa");
			Calendar begin = Calendar.getInstance();
			begin.add(Calendar.MINUTE, -7);
			australia.setOpen(begin);
			Calendar close2 = Calendar.getInstance();
			close2.add(Calendar.MINUTE, -5);
			australia.setClose(close2);
			season.addRace(australia);
			australia.addBid(helper.getBid(flb, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 100000));
			fail("Game is not open");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void testDoubleAddBid() {
		Bid bid = helper.getBid(flb, driver2, driver1, driver3, driver4, driver5, driver6, 1, 1, 100000);
		monza.addBid(bid);
		getEntityManager().flush();
		assertNotNull(bid.getId());
		try {
			bid = helper.getBid(flb, driver2, driver1, driver3, driver5, driver4, driver6, 1, 1, 100000);
			monza.addBid(bid);
			fail("Double bid from same player should fail");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void testGetPointsForNonClosedRace() {
		Bid bid = helper.getBid(flb, driver2, driver1, driver3, driver4, driver5, driver6, 1, 1, 100000);
		monza.addBid(bid);
		getEntityManager().flush();
		assertNotNull(bid.getId());
		try {
			bid.getPoints();
			fail("Points not available, since race is not completed");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	
	public void testValidDates() {
		monza.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		try {
			monza.setClose(close);
		} catch (IllegalStateException e) {
			fail("Dates should have been ok");
		}
	}
	
	public void testInvalidDates() {
		monza.setOpen(Calendar.getInstance());
		monza.getOpen().add(Calendar.MONTH, 1);
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
    		monza.completeRace(raceResultMonza);
    	} catch (IllegalStateException expected) {
    	}
    }

    public void testRaceResultGame() throws Exception {
    	monza.addBid(flbMonzaBid);
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.completeRace(raceResultMonza);
   		getEntityManager().flush();
   		assertNotNull(raceResultMonza.getId());
    }

    public void testResult() throws Exception{
    	
    	assertEquals(BigDecimal.ZERO, bookie.getAccount().getBalance());
    	
    	monza.addBid(flbMonzaBid);
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	assertEquals(new BigDecimal(60), bookie.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), flb.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), mba.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), ttp.getAccount().getBalance());
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.completeRace(raceResultMonza);
   		getEntityManager().flush();
    	
   		assertNotNull(flbMonzaBid.getId());
   		assertNotNull(mbaMonzaBid.getId());
   		assertNotNull(ttpMonzaBid.getId());
 		assertNotNull(raceResultMonza.getId());
 		
 		List<Bid> result = monza.getResult();
 		assertEquals(3, result.size());
 		assertEquals(flbMonzaBid, result.get(0));
 		assertEquals(mbaMonzaBid, result.get(1));
 		assertEquals(ttpMonzaBid, result.get(2));
 		
    	assertEquals(BigDecimal.ZERO, bookie.getAccount().getBalance());
    	assertEquals(new BigDecimal(140), flb.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), mba.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), ttp.getAccount().getBalance());
    }
    
    public void testResultRolledback() throws Exception{
    	testResult();
    	
    	monza.rollbackRace();
    	
    	assertFalse(monza.isCompleted());
    	
    	try {
    		monza.getResult();
    		fail("Result should not have been available");
    	} catch (IllegalStateException e) {
    		// Since race is not completed the result should not be available
    	}
    	
    	assertEquals(new BigDecimal(60), bookie.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), flb.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), mba.getAccount().getBalance());
    	assertEquals(new BigDecimal(80), ttp.getAccount().getBalance());
    	
    }
    
	

    public void testResultBetterPole() throws Exception{
    	monza.addBid(flbMonzaBid);
    	mbaMonzaBid = helper.getBid(mba, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 11000);
		
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.completeRace(raceResultMonza);
   		getEntityManager().flush();
    	
   		assertNotNull(flbMonzaBid.getId());
   		assertNotNull(mbaMonzaBid.getId());
   		assertNotNull(ttpMonzaBid.getId());
 		assertNotNull(raceResultMonza.getId());
 		
 		List<Bid> result = monza.getResult();
 		assertEquals(3, result.size());
 		assertEquals(flbMonzaBid, result.get(0));
 		assertEquals(mbaMonzaBid, result.get(1));
 		assertEquals(ttpMonzaBid, result.get(2));
    }
    
}
