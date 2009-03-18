package dk.bregnvig.formula1.event;

import java.math.BigDecimal;
import java.util.Calendar;

import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.DummyMailServiceImpl;

public class NoMoreMoneyReminderTest extends AbstractDaoTest {

	private DummyMailServiceImpl mailService;

	public void testMailsAreSend() throws Exception {
		
		mailService.players.clear();
		
		Calendar close = Calendar.getInstance();
		close.add(Calendar.SECOND, 1);
		monza.setClose(close);
		ttp.getAccount().setBalance(new BigDecimal(ttp.getAccount().getMinBalance() + 20));
		
		monza.addBid(flbMonzaBid);
		monza.addBid(mbaMonzaBid);
		monza.addBid(ttpMonzaBid);
		
		Thread.sleep(1000);
		
		monza.completeRace(raceResultMonza);
		assertEquals(1, mailService.players.size());
		assertEquals(true, mailService.players.contains(ttp));
	}
	
	public void setMailService(DummyMailServiceImpl mailService) {
		this.mailService = mailService;
	}
	
}
