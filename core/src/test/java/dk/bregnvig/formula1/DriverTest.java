package dk.bregnvig.formula1;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.util.AbstractDaoTest;
import dk.bregnvig.formula1.util.CreatorHelper;

public class DriverTest extends AbstractDaoTest {

	private Driver schumacher;
	private CreatorHelper helper = new CreatorHelper();
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		schumacher = helper.getDriver(1, "Michael Schumacher");
		getEntityManager().persist(schumacher);
	}
	
	public void testHashCode() {
		Driver copy = helper.getDriver(1, "Michael Schumacher");
		assertEquals(schumacher.hashCode(), copy.hashCode());
		
		Driver another = helper.getDriver(2, "Felipe Massa");
		assertFalse(schumacher.hashCode() == another.hashCode());
	}

	public void testEqualsObject() {
		Driver copy = helper.getDriver(1, "Michael Schumacher");
		assertEquals(schumacher, copy);
		
		Driver another = helper.getDriver(2, "Felipe Massa");
		assertFalse(schumacher.equals(another));
	}

}
