package dk.bregnvig.formula1;

import javax.persistence.PersistenceException;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class SeasonTest extends AbstractDaoTest {
	
	public SeasonTest() {
		setDefaultRollback(true);
	}
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	public void testInitialized() {
		assertNotNull(getEntityManager());
		assertNotNull(season.getDao());
	}
	
	public void testPlayersAdded() {

		season = getEntityManager().find(Season.class, season.getId());
		
		assertTrue(season.getPlayers().contains(flb));
		assertTrue(season.getPlayers().contains(mba));
		assertTrue(season.getPlayers().contains(ttp));
	}

	public void testDriversAdded() {

		season = getEntityManager().find(Season.class, season.getId());
		
		assertTrue(season.getDrivers().contains(driver1));
		assertTrue(season.getDrivers().contains(driver2));
		assertTrue(season.getDrivers().contains(driver3));
	}
	
	public void testFindPlayer() {
		assertNotNull(season.getPlayer("flb"));
	}

	public void testPlayersStay() {
		
		assertNotNull(getEntityManager().find(Player.class, flb.getId()));

		getEntityManager().flush();
		getEntityManager().remove(season);
		
		assertNotNull(getEntityManager().find(Player.class, flb.getId()));
		assertNotNull(getEntityManager().find(Player.class, mba.getId()));
		assertNotNull(getEntityManager().find(Player.class, ttp.getId()));
		assertNull(getEntityManager().find(Season.class, season.getId()));
	}
	
	public void testPlayerNotCreated() {
		try {
			season.addPlayer(helper.getPlayer("notpersisted"));
			getEntityManager().persist(season);
			getEntityManager().flush();
			fail("Player not created. Should fail");
		} catch (IllegalStateException expected) {
			
		}
	}
	
	public void testPlayersDisappearsFromSeason() {
		
		season.removePlayer(flb);
		getEntityManager().remove(flb);
		getEntityManager().flush();
		
		season = getEntityManager().find(Season.class, season.getId());
		assertEquals(2, season.getPlayers().size());
	}
	
	public void testRacesCreated() {
		
		Race montreal = helper.getRace("Montreal");
		montreal.setSelectedDriver(driver1);
		Race indianpolis = helper.getRace("Indianpolis");
		indianpolis.setSelectedDriver(driver2);
		Race brazil = helper.getRace("Brazil");
		brazil.setSelectedDriver(driver3);
		
		getEntityManager().persist(season);
		getEntityManager().flush();

		season.addRace(montreal);
		season.addRace(indianpolis);
		season.addRace(brazil);
		
		getEntityManager().flush();
		
		
		assertNotNull(montreal);
		assertNotNull(indianpolis);
		assertNotNull(brazil);
	}
	
	public void testMustHaveName() {
		season.setName(null);
		try {
			getEntityManager().merge(season);
			getEntityManager().flush();
			fail("Must have name");
		} catch (PersistenceException expected) {
			
		}
	}
}
