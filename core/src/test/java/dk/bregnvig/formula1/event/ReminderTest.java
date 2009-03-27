package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.DummyMailServiceImpl;
import dk.bregnvig.formula1.util.DummySMSGatewayImpl;

public class ReminderTest extends AbstractDaoTest {

	private OneWeekReminderService oneWeekService;
	private TwoHourReminderService twoHoursService;
	private DummyMailServiceImpl mailService;
	private DummySMSGatewayImpl gateway;
	
	public void testInitialized() {
		assertNotNull(oneWeekService);
		assertNotNull(twoHoursService);
		assertNotNull(mailService);
	}
	
	public void testLetterSending() {
		mailService.players.clear();
		monza.addBid(ttpMonzaBid);
		oneWeekService.invoke(monza);
		assertEquals(2, mailService.players.size());
		assertEquals(true, mailService.players.contains(flb));
		assertEquals(true, mailService.players.contains(mba));
	}

	public void testSMSSending() {
		gateway.getNumbers().clear();
		gateway.getMessages().clear();
		monza.addBid(ttpMonzaBid);
		twoHoursService.invoke(monza);
		assertEquals(2, gateway.getNumbers().size());
		assertEquals(true, gateway.getNumbers().contains(flb.getSms()));
		assertEquals(true, gateway.getNumbers().contains(mba.getSms()));
	}

	public void setOneWeekReminderService(OneWeekReminderService service) {
		this.oneWeekService = service;
	}

	public void setTwoHoursReminderService(TwoHourReminderService service) {
		this.twoHoursService = service;
	}

	public void setMailService(DummyMailServiceImpl mailService) {
		this.mailService = mailService;
	}

	public void setSMSGateway(DummySMSGatewayImpl gateway) {
		this.gateway = gateway;
	}
}
