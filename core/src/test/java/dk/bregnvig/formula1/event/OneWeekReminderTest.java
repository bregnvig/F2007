package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.DummyMailServiceImpl;

public class OneWeekReminderTest extends AbstractDaoTest {

	private OneWeekReminderService service;
	private DummyMailServiceImpl mailService;
	
	public void testInitialized() {
		assertNotNull(service);
		assertNotNull(mailService);
	}
	
	public void testLetterSending() {
		mailService.players.clear();
		monza.addBid(ttpMonzaBid);
		service.invoke(monza);
		assertEquals(2, mailService.players.size());
		assertEquals(true, mailService.players.contains(flb));
		assertEquals(true, mailService.players.contains(mba));
	}

	public void setOneWeekReminderService(OneWeekReminderService service) {
		this.service = service;
	}

	public void setMailService(DummyMailServiceImpl mailService) {
		this.mailService = mailService;
	}
}
