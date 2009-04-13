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
		
		bid.setPosition1(driver1);
		bid.setPosition2(driver1);
		bid.setPosition3(driver1);
		bid.setPosition4(driver1);
		bid.setPosition5(driver1);
		bid.setPosition6(driver1);
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
		
		other.setPosition1(driver1);
		other.setPosition2(driver1);
		other.setPosition3(driver1);
		other.setPosition4(driver1);
		other.setPosition5(driver1);
		other.setPosition6(driver1);
		
		assertEquals(bid.hashCode(), other.hashCode());
		
		other.setPosition1(driver2);
		assertFalse(bid.hashCode() == other.hashCode());
	}

	public void testEquals() {
		getEntityManager().persist(bid);
		GridBid other = new GridBid();
		
		other.setPosition1(driver1);
		other.setPosition2(driver1);
		other.setPosition3(driver1);
		other.setPosition4(driver1);
		other.setPosition5(driver1);
		other.setPosition6(driver1);
		
		assertEquals(bid, other);
		
		other.setPosition1(driver2);
		assertFalse(bid.equals(other));
	}
}
