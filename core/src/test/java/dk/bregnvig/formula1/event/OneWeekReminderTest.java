package dk.bregnvig.formula1.event;

import org.springframework.context.ApplicationContext;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class OneWeekReminderTest extends AbstractDaoTest {

	private ApplicationContext context;
	private OneWeekReminderService service;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		context = getApplicationContext();
		service.setRace(monza);
	}
	
	public void testInitialized() {
		assertNotNull(context);
		assertNotNull(service);
	}
	
	public void testLetterSending() {
		monza.addBid(ttpMonzaBid);
		
		service.invoke();
	}

	public void setOneWeekReminderService(OneWeekReminderService service) {
		this.service = service;
	}
	
	
	

}
