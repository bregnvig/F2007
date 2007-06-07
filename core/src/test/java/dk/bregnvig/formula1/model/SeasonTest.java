package dk.bregnvig.formula1.model;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.CreatorHelper;

public class SeasonTest extends AbstractDaoTest {
	
	private CreatorHelper helper = new CreatorHelper();
	
	private Season season;
	
	private Player flb;
	private Player mba;
	private Player ttp;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		getEntityManager().persist(flb = helper.getPlayer("flb"));
		getEntityManager().persist(mba = helper.getPlayer("mba"));
		getEntityManager().persist(ttp = helper.getPlayer("ttp"));
		season = new Season();
		season.setName("F1 2007");
	}
	
	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testPlayersAdded() {
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		
		getEntityManager().persist(season);
		
		getEntityManager().flush();

		season = getEntityManager().find(Season.class, season.getId());
		
		assertTrue(season.getPlayers().contains(flb));
		assertTrue(season.getPlayers().contains(mba));
		assertTrue(season.getPlayers().contains(ttp));
	}
	
	public void testPlayersStay() {
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		
		getEntityManager().persist(season);
		
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
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		
		getEntityManager().persist(season);
		
		season.removePlayer(flb);
		getEntityManager().remove(flb);
		getEntityManager().flush();
		
		season = getEntityManager().find(Season.class, season.getId());
		assertEquals(2, season.getPlayers().size());
	}
	
	public void testRacesCreated() {
		Race montreal = helper.getRace("Montreal"); 
		Race indianpolis = helper.getRace("Indianpolis"); 
		Race brazil = helper.getRace("Brazil"); 
		
		season.addRace(montreal);
		season.addRace(indianpolis);
		season.addRace(brazil);
		
		getEntityManager().persist(season);
		
		assertNotNull(montreal);
		assertNotNull(indianpolis);
		assertNotNull(brazil);
	}
}
