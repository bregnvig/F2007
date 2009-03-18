package dk.bregnvig.formula1.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class EventServiceTest extends AbstractDaoTest {

    public void testRaceTimersAreNotified() throws Exception {
    	
    	List<RaceTimer> backup = eventService.getTimers();
    	List<RaceTimer> timers = new ArrayList<RaceTimer>();
    	TestRaceTimer timer = new TestRaceTimer(); 
    	timers.add(timer);
    	
    	eventService.setTimers(timers);
    	eventService.initialized();

    	Thread.sleep(500);
    	
    	assertEquals(true, timer.monzaInvoked);
    	eventService.setTimers(backup);
    	eventService.initialized();
    }


	private class TestRaceTimer implements RaceTimer {
		
		private boolean monzaInvoked;

		public int getDelay(Race race) {
			if (race.getName().equals(monza.getName())) return 300;
			return -1;
		}

		public void invoke(Race race) {
			if (race.getName().equals(monza.getName())) {
				monzaInvoked = true;
			}
		}
		
		
		
	}
	
	public void testListener() throws Exception {
    	Calendar open = Calendar.getInstance();
    	open.add(Calendar.SECOND, 1);
    	Calendar close = Calendar.getInstance();
    	close.add(Calendar.SECOND, 3);
    	spa.setOpen(open);
    	spa.setClose(close);
    	List<RaceListener> backup = eventService.getListeners();
    	List<RaceListener> listeners = new ArrayList<RaceListener>();
    	TestRaceListener listener = new TestRaceListener();
    	listeners.add(listener);
    	
    	eventService.setListeners(listeners);
    	eventService.initialized();
    	
    	Thread.sleep(1000);
    	spa.addBid(flbSpaBid);
    	Thread.sleep(3000);
    	spa.completeRace(raceResultSpa);
    	
    	assertEquals(spa.getOpen().get(Calendar.SECOND), listener.getOpened().get(Calendar.SECOND));
    	assertEquals(spa.getClose().get(Calendar.SECOND), listener.getClosed().get(Calendar.SECOND));
    	assertNotNull(listener.getCompleted());
    	
    	eventService.setListeners(backup);
    	eventService.initialized();
	}
	
	private class TestRaceListener implements RaceListener {
		
		private Calendar opened;
		private Calendar closed;
		private Calendar completed;

		public Calendar getClosed() {
			return closed;
		}

		public Calendar getCompleted() {
			return completed;
		}

		public Calendar getOpened() {
			return opened;
		}

		public void raceClosed(Race race) {
			closed = Calendar.getInstance();
		}

		public void raceCompleted(Race race) {
			completed = Calendar.getInstance();
		}

		public void raceOpened(Race race) {
			opened = Calendar.getInstance();
		}
		
	}

}
