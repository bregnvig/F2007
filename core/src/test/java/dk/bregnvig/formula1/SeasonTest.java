package dk.bregnvig.formula1;

import org.springframework.transaction.UnexpectedRollbackException;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class SeasonTest extends AbstractDaoTest {
	
	public SeasonTest() {
		setDefaultRollback(true);
	}
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testPlayersAdded() {

		season = getEntityManager().find(Season.class, season.getId());
		
		assertTrue(season.getPlayers().contains(flb));
		assertTrue(season.getPlayers().contains(mba));
		assertTrue(season.getPlayers().contains(ttp));
	}

	public void testDriversAdded() {

		season = getEntityManager().find(Season.class, season.getId());
		
		assertTrue(season.getDrivers().contains(kimi));
		assertTrue(season.getDrivers().contains(massa));
		assertTrue(season.getDrivers().contains(hamilton));
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
		montreal.setSelectedDriver(kimi);
		Race indianpolis = helper.getRace("Indianpolis");
		indianpolis.setSelectedDriver(massa);
		Race brazil = helper.getRace("Brazil");
		brazil.setSelectedDriver(hamilton);
		
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
		season.setName("w");
		try {
			getEntityManager().persist(season);
//			fail("Must have name");
		} catch (UnexpectedRollbackException expected) {
			
		}
	}
}
