package dk.bregnvig.formula1.model;

import java.util.Calendar;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class RaceTest extends AbstractDaoTest {
	
	private Race race = new Race();

	@Override
	protected void onSetUpInTransaction() throws Exception {
		race.setName("Monaco");
		race.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		race.setClose(close);
		getEntityManager().persist(race);
	}
	
	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testRaceCreated() {
		assertNotNull(race.getId());
	}

	public void testRaceFind() {
		assertNotNull(getEntityManager().find(Race.class, race.getId()));
	}
	
	public void testDeleteRace() {
		getEntityManager().remove(race);
		assertNull(getEntityManager().find(Race.class, race.getId()));
	}
	
	public void testValidDates() {
		race.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MONTH, 1);
		try {
			race.setClose(close);
		} catch (IllegalStateException e) {
			fail("Dates should have been ok");
		}
	}
	
	public void testInvalidDates() {
		race.setOpen(Calendar.getInstance());
		race.getOpen().add(Calendar.MONTH, 1);
		Calendar close = Calendar.getInstance();
		try {
			race.setClose(close);
			fail("Dates should have failed ok");
		} catch (IllegalStateException expected) {
		}
	}
}
