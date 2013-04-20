package dk.bregnvig.formula1.scraping.impl;

import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.scraping.impl.AutomaticResultImpl;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class AutomaticRaceResultTest extends AbstractDaoTest {
	
	private AutomaticResultImpl autoResult;

	public void setAutoResult(AutomaticResultImpl autoResult) {
		this.autoResult = autoResult;
	}
	
	public void testInitialized() {
		assertNotNull(autoResult);
	}

	public void testURL() {
		autoResult.setURL("http://www.formula1.com/results/season/2009/806/6609/results.html");
		assertEquals("http://www.formula1.com/results/season/2009/806/6609/", autoResult.getResultURL());
		assertEquals("http://www.formula1.com/results/season/2009/806/6609/", autoResult.getFastestLapURL());
		assertEquals("http://www.formula1.com/results/season/2009/806/6608/", autoResult.getQualifyURL());
	}
	
	public void testResultMalaysia() {
		autoResult.setURL("http://www.formula1.com/results/season/2009/806/6609/results.html");
		RaceResult result = autoResult.getRaceResult(monza);
		assertEquals(result.getGrid().getPosition1(), driver22);
		assertEquals(result.getGrid().getPosition2(), driver9);
		assertEquals(result.getGrid().getPosition3(), driver15);
		assertEquals(result.getGrid().getPosition4(), driver23);
		assertEquals(result.getGrid().getPosition5(), driver10);
		assertEquals(result.getGrid().getPosition6(), driver16);
		assertEquals(result.getGrid().getPosition7(), driver14);
		
		assertEquals(result.getFastestLap().getDriver(), driver22);
		
		assertEquals(result.getPodium().getPosition1(), driver22);
		assertEquals(result.getPodium().getPosition2(), driver6);
		assertEquals(result.getPodium().getPosition3(), driver10);
		assertEquals(result.getPodium().getPosition4(), driver9);
		
		assertEquals(12, result.getSelectedDriver().getStartPosition());
		assertEquals(7, result.getSelectedDriver().getEndPosition());
		
		assertEquals(driver2, result.getFirstCrash().getCrash1());
		assertEquals(driver5, result.getFirstCrash().getCrash2());
		assertEquals(null, result.getFirstCrash().getCrash3());
	}

	public void testResultAustralia() {
		autoResult.setURL("http://www.formula1.com/results/season/2009/805/6603/");
		RaceResult result = autoResult.getRaceResult(monza);
		assertEquals(result.getGrid().getPosition1(), driver22);
		assertEquals(result.getGrid().getPosition2(), driver23);
		assertEquals(result.getGrid().getPosition3(), driver15);
		assertEquals(result.getGrid().getPosition4(), driver5);
		assertEquals(result.getGrid().getPosition5(), driver16);
		assertEquals(result.getGrid().getPosition6(), driver3);
		assertEquals(result.getGrid().getPosition7(), driver4);
		
		assertEquals(result.getFastestLap().getDriver(), driver16);
		
		assertEquals(result.getPodium().getPosition1(), driver22);
		assertEquals(result.getPodium().getPosition2(), driver23);
		assertEquals(result.getPodium().getPosition3(), driver9);
		assertEquals(result.getPodium().getPosition4(), driver10);
		
		assertEquals(18, result.getSelectedDriver().getStartPosition());
		assertEquals(20, result.getSelectedDriver().getEndPosition());
		
		assertEquals(result.getFirstCrash().getCrash1(), driver2);
		assertEquals(result.getFirstCrash().getCrash2(), driver17);
		assertEquals(result.getFirstCrash().getCrash3(), driver8);
	}

}
