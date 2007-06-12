package dk.bregnvig.formula1;

import java.util.Calendar;

import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class RaceTest extends AbstractDaoTest {
	
	private Race race = new Race();
	
	private Season season;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		race.setSelectedDriver(kimi);
		race.setName("Monaco");
		race.setBegin(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		race.setClose(close);
		
		season = new Season();
		season.setName("F1 2007");
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		season.addRace(race);
		getEntityManager().persist(season);
		
	}
	
	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testRaceCreated() {
		assertNotNull(race.getId());
		assertNotNull(race.getSeason());
	}

	public void testRaceFind() {
		assertNotNull(getEntityManager().find(Race.class, race.getId()));
	}
	
	public void testDeleteRace() {
		getEntityManager().remove(race);
		assertNull(getEntityManager().find(Race.class, race.getId()));
	}
	
	public void testValidDates() {
		race.setBegin(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		try {
			race.setClose(close);
		} catch (IllegalStateException e) {
			fail("Dates should have been ok");
		}
	}
	
	public void testInvalidDates() {
		race.setBegin(Calendar.getInstance());
		race.getBegin().add(Calendar.MONTH, 1);
		Calendar close = Calendar.getInstance();
		try {
			race.setClose(close);
			fail("Dates should have failed ok");
		} catch (IllegalStateException expected) {
		}
	}
	
    public void testRaceConfiguration() {
        Race race = new Race();
        assertNotNull("Result strategy should have been provided", race.getResultStrategy());
    }
}
