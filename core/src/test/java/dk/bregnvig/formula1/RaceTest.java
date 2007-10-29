package dk.bregnvig.formula1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.event.AbstractRaceListener;
import dk.bregnvig.formula1.event.AbstractRaceTimer;
import dk.bregnvig.formula1.event.RaceTimer;
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
		player.setPassword("fie");
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
			australia.setOpen(begin);
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
    		monza.setRaceResult(raceResultMonza);
    	} catch (IllegalStateException expected) {
    	}
    }

    public void testRaceResultGame() throws Exception {
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResultMonza);
   		getEntityManager().flush();
   		assertNotNull(raceResultMonza.getId());
    }

    public void testResult() throws Exception{
    	monza.addBid(flbMonzaBid);
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResultMonza);
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
    
    public void testRaceTimersAreNotified() throws Exception {
    	
    	final List<Race> races = new ArrayList<Race>();
    	Calendar begin = Calendar.getInstance();
    	begin.add(Calendar.SECOND, 5);
    	List<RaceTimer> timers = new ArrayList<RaceTimer>();
    	timers.add(new TestRaceTimer(races));
    	spa.setTimers(timers);
    	spa.setOpen(begin);
    	
    	Thread.sleep(5000);
    	
    	assertEquals(spa, races.get(0));
    }


	private class TestRaceTimer extends AbstractRaceTimer {

		private List<Race> races;
		
		public TestRaceTimer(List<Race> races) {
			this.races = races;
			setSeconds(3);
		}
		
		public void invoke() {
			races.add(getRace());
		}
	}
	
	public void testNotTheSameTimer() {
		for (int i = 0; i < spa.getTimers().size(); i++) {
			assertFalse(spa.getTimers().get(i) == monza.getTimers().get(i));	
		}
	}

	public void testNotTheSameListeners() {
		for (int i = 0; i < spa.getListeners().size(); i++) {
			assertFalse(spa.getListeners().get(i) == monza.getListeners().get(i));	
		}
	}
	
	public void testListener() throws Exception {
    	Calendar open = Calendar.getInstance();
    	open.add(Calendar.SECOND, 1);
    	Calendar close = Calendar.getInstance();
    	close.add(Calendar.SECOND, 3);
    	List<AbstractRaceListener> listners = new ArrayList<AbstractRaceListener>();
    	TestRaceListener listener = new TestRaceListener();
    	listners.add(listener);
    	spa.setOpen(null);
    	spa.setClose(null);
    	spa.setListeners(listners);
    	spa.setOpen(open);
    	spa.setClose(close);
    	Thread.sleep(4000);
    	spa.setRaceResult(raceResultMonza);
    	
    	assertEquals(spa.getOpen().get(Calendar.SECOND), listener.getOpened().get(Calendar.SECOND));
    	assertEquals(spa.getClose().get(Calendar.SECOND), listener.getClosed().get(Calendar.SECOND));
    	assertNotNull(listener.getCompleted());
	}
	
	private class TestRaceListener extends AbstractRaceListener {
		
		private Calendar opened;
		private Calendar closed;
		private Calendar completed;

		@Override
		public void raceClosed() {
			closed = Calendar.getInstance();
		}

		@Override
		public void raceCompleted() {
			completed = Calendar.getInstance();
		}

		@Override
		public void raceOpened() {
			opened = Calendar.getInstance();
		}

		public Calendar getClosed() {
			return closed;
		}

		public Calendar getCompleted() {
			return completed;
		}

		public Calendar getOpened() {
			return opened;
		}
		
	}
	

    public void testResultBetterPole() throws Exception{
    	monza.addBid(flbMonzaBid);
    	mbaMonzaBid = helper.getBid(mba, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 11000);
		
    	monza.addBid(mbaMonzaBid);
    	monza.addBid(ttpMonzaBid);
    	
    	Thread.sleep(200);
    	monza.setClose(Calendar.getInstance());
    	Thread.sleep(200);
   		monza.setRaceResult(raceResultMonza);
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
