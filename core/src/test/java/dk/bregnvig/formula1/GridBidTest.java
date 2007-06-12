package dk.bregnvig.formula1;

import javax.persistence.PersistenceException;

import dk.bregnvig.formula1.bid.GridBid;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class GridBidTest extends AbstractDaoTest {

	private GridBid bid;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		bid = new GridBid();
		
		bid.setPosition1(kimi);
		bid.setPosition2(kimi);
		bid.setPosition3(kimi);
		bid.setPosition4(kimi);
		bid.setPosition5(kimi);
		bid.setPosition6(kimi);
	}
	
	public void testCreated() {
		getEntityManager().persist(bid);
		assertNotNull(bid.getId());
	}

	public void testPositionIsMissing() {
		bid.setPosition1(null);
		try {
			getEntityManager().persist(bid);
			fail("Should not have been saved based. Position is missing");
		} catch (PersistenceException expected) {
		}
	}

	public void testHashcode() {
		getEntityManager().persist(bid);
		GridBid other = new GridBid();
		
		other.setPosition1(kimi);
		other.setPosition2(kimi);
		other.setPosition3(kimi);
		other.setPosition4(kimi);
		other.setPosition5(kimi);
		other.setPosition6(kimi);
		
		assertEquals(bid.hashCode(), other.hashCode());
		
		other.setPosition1(massa);
		assertFalse(bid.hashCode() == other.hashCode());
	}

	public void testEquals() {
		getEntityManager().persist(bid);
		GridBid other = new GridBid();
		
		other.setPosition1(kimi);
		other.setPosition2(kimi);
		other.setPosition3(kimi);
		other.setPosition4(kimi);
		other.setPosition5(kimi);
		other.setPosition6(kimi);
		
		assertEquals(bid, other);
		
		other.setPosition1(massa);
		assertFalse(bid.equals(other));
	}
}
