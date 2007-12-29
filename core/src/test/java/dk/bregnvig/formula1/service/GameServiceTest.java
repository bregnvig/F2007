package dk.bregnvig.formula1.service;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class GameServiceTest extends AbstractDaoTest {
	
	private GameService service;
	
	public GameServiceTest() {
		setDefaultRollback(true);
	}
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	public void testInitialize() {
		assertNotNull(service);
		assertNotNull(getEntityManager());
	}
	
	public void testSeason() {
		season = service.findByName("F1 2007");
		assertNotNull(season);
	}
	
	public void testPlayersAdded() {

		season = service.findByName("F1 2007");
		
		assertNotNull(season);
		assertNotNull(season.getDao());
		
		assertTrue(season.getPlayers().contains(flb));
		assertTrue(season.getPlayers().contains(mba));
		assertTrue(season.getPlayers().contains(ttp));
	}
	
	public void testUpdatePlayer() {
		
		assertFalse("newpass".equals(flb.getPassword()));
		
		flb.setPassword("newpass");
		
		service.updatePlayer(flb);
		
		getEntityManager().flush();
		
		season = service.findByName("F1 2007");
		flb = season.getPlayer("flb");
		
		assertNotNull(flb);
		assertEquals("newpass", flb.getPassword());
	}


	public void setService(GameService service) {
		this.service = service;
	}
}
