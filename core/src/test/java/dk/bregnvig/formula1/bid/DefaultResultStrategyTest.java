package dk.bregnvig.formula1.bid;

import java.util.HashSet;
import java.util.Set;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.util.CreatorHelper;
import junit.framework.TestCase;

public class DefaultResultStrategyTest extends TestCase {

	private DefaultResultStrategy strategy = new DefaultResultStrategy();
	
	protected Driver kimi;
	protected Driver massa;
	protected Driver hamilton;
	protected Driver alonso;
	protected Driver heidfeld;
	protected Driver kubica;
	protected Driver button;
	
	private Bid firstBid;
	private Bid secondBid;
	private Bid thirdBid;
	
	protected Player flb;
	protected Player mba;
	protected Player ttp;
	
	private RaceResult raceResult;
	private Set<Bid> bids;

	protected CreatorHelper helper = new CreatorHelper();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		flb = helper.getPlayer("flb");
		ttp = helper.getPlayer("ttp");
		mba = helper.getPlayer("mba");
		
		kimi = helper.getDriver(6, "Kimi Raikonen");
		massa = helper.getDriver(5, "Felipe Massa");
		hamilton = helper.getDriver(2, "Lewis Hamilton");
		alonso = helper.getDriver(1, "Wanker Alonso");
		heidfeld = helper.getDriver(7, "Nick Heidfeld");
		kubica = helper.getDriver(8, "Robert Kubica");
		button = helper.getDriver(19, "Jenson Button");
		
		firstBid = helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 10000);
		secondBid = helper.getBid(mba, massa, kimi, hamilton, alonso, kubica, heidfeld, 2, 2, 20000);
		thirdBid = helper.getBid(ttp, heidfeld, kubica, alonso, hamilton, kimi, massa, 3, 3, 20000);
		
		bids = new HashSet<Bid>();
		bids.add(firstBid);
		bids.add(secondBid);
		bids.add(thirdBid);
		
		raceResult = helper.getRaceResult(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, button, 1, 1, 10000);
		
	}

	public void testCalculateGridResult() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(12, firstBid.getGrid().getPoints());
		assertEquals(8, secondBid.getGrid().getPoints());
		assertEquals(2, thirdBid.getGrid().getPoints());
	}

	public void testCalculatePodiumResult() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(6, firstBid.getPodium().getPoints());
		assertEquals(4, secondBid.getPodium().getPoints());
		assertEquals(1, thirdBid.getPodium().getPoints());
	}

	public void testCalculateFastestLapResult() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(2, firstBid.getFastestLap().getPoints());
		assertEquals(0, secondBid.getFastestLap().getPoints());
		assertEquals(0, thirdBid.getFastestLap().getPoints());
	}
	
	public void testCalculateFirstCrash() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(3, firstBid.getFirstCrash().getPoints());
		assertEquals(2, secondBid.getFirstCrash().getPoints());
		assertEquals(0, thirdBid.getFirstCrash().getPoints());
	}
	
	public void testCalculateSelectedDriver() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(6, firstBid.getSelectedDriver().getPoints());
		assertEquals(4, secondBid.getSelectedDriver().getPoints());
		assertEquals(2, thirdBid.getSelectedDriver().getPoints());
	}
	
	public void testCalculateResult() {
		strategy.calculateResult(raceResult, bids);
		
		assertEquals(29, firstBid.getPoints());
		assertEquals(18, secondBid.getPoints());
		assertEquals(5, thirdBid.getPoints());
	}
	
}
