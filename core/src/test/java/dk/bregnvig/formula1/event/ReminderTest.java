package dk.bregnvig.formula1.event;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.DummyMailServiceImpl;
import dk.bregnvig.formula1.util.DummySMSGatewayImpl;

public class ReminderTest extends AbstractDaoTest {

	private RaceTimer oneWeekService;
	private RaceTimer twoHoursService;
//	private JavaMailSender mailService;
	private DummyMailServiceImpl mailService;
	private DummySMSGatewayImpl gateway;
	
	public ReminderTest() {
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	public void testInitialized() {
		assertNotNull(oneWeekService);
		assertNotNull(twoHoursService);
		assertNotNull(mailService);
	}
	
	public void testLetterSending() {
		mailService.players.clear();
		monza.addBid(ttpMonzaBid);
		getEntityManager().flush();
		oneWeekService.invoke(monza);
		assertEquals(2, mailService.players.size());
		assertEquals(true, mailService.players.contains(flb));
		assertEquals(true, mailService.players.contains(mba));
	}

	public void testSMSSending() {
		gateway.getNumbers().clear();
		gateway.getMessages().clear();
		monza.addBid(ttpMonzaBid);
		getEntityManager().flush();
		twoHoursService.invoke(monza);
		assertEquals(2, gateway.getNumbers().size());
		assertEquals(true, gateway.getNumbers().contains(flb.getSms()));
		assertEquals(true, gateway.getNumbers().contains(mba.getSms()));
	}

	public void setEmailReminderService(RaceTimer service) {
		this.oneWeekService = service;
	}

	public void setSmsReminderService(RaceTimer service) {
		this.twoHoursService = service;
	}

	public void setMailSender(DummyMailServiceImpl mailService) {
		this.mailService = mailService;
	}

	public void setSmsGateway(DummySMSGatewayImpl gateway) {
		this.gateway = gateway;
	}
}