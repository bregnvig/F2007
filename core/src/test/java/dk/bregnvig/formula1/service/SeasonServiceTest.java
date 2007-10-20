package dk.bregnvig.formula1.service;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class SeasonServiceTest extends AbstractDaoTest {
	
	private SeasonService service;
	
	public SeasonServiceTest() {
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
	
	public void testPlayersAdded() {

		season = service.findByName("F1 2007");
		
		assertNotNull(season);
		
		assertTrue(season.getPlayers().contains(flb));
		assertTrue(season.getPlayers().contains(mba));
		assertTrue(season.getPlayers().contains(ttp));
	}

	public void setService(SeasonService service) {
		this.service = service;
	}
}
