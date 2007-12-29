package dk.bregnvig.formula1.account;

import java.math.BigDecimal;
import java.util.List;

import dk.bregnvig.formula1.account.Account.Entry;
import dk.bregnvig.formula1.util.AbstractDaoTest;

public class AccountTest extends AbstractDaoTest {
	
	private Account accountA = new Account();
	private Account accountB = new Account();
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		accountA.setBalance(new BigDecimal(100));
		accountB.setBalance(new BigDecimal(10));
		
		getEntityManager().persist(accountA);
		getEntityManager().persist(accountB);
	}

	public void testEntityManagerThere() {
		assertNotNull(getEntityManager());
	}
	
	public void testCreated() {
		assertNotNull(accountA.getId());
		assertNotNull(accountB.getId());
		
		assertNotNull(getEntityManager().find(Account.class, accountA.getId()));
		assertNotNull(getEntityManager().find(Account.class, accountB.getId()));
	}
	
	public void testDeposit() {
		accountA.deposit("Overført af Flemming", new BigDecimal(100));
		getEntityManager().flush();
		assertEquals(new BigDecimal(200), getEntityManager().find(Account.class, accountA.getId()).getBalance());
		Account.Entry entry = accountA.getEntries().iterator().next();
		assertEquals("Overført af Flemming", entry.getMessage());
		assertEquals(new BigDecimal(100), entry.getAmount());
	}

	public void testWithdraw() {
		accountA.withdraw("Trukket af Flemming", new BigDecimal(100));
		getEntityManager().flush();
		assertEquals(new BigDecimal(0), getEntityManager().find(Account.class, accountA.getId()).getBalance());
		Account.Entry entry = accountA.getEntries().iterator().next();
		assertEquals("Trukket af Flemming", entry.getMessage());
		assertEquals(new BigDecimal(-100), entry.getAmount());
	}
	
	public void testAccountHasId() {
		assertNotNull(flb.getAccount().getId());
	}

	public void testTransfer() {
		accountA.transfer("Overført til account b af Flemming", new BigDecimal(100), accountB);
		getEntityManager().flush();
		assertEquals(new BigDecimal(0), getEntityManager().find(Account.class, accountA.getId()).getBalance());
		assertEquals(new BigDecimal(110), getEntityManager().find(Account.class, accountB.getId()).getBalance());
		Account.Entry entryA = accountA.getEntries().iterator().next();
		Account.Entry entryB = accountB.getEntries().iterator().next();
		assertTrue(entryA == entryB);
		assertEquals("Overført til account b af Flemming", entryA.getMessage());
		assertEquals(new BigDecimal(100), entryA.getAmount());
	}
	
	public void testEntriesWithFlush() throws Exception {
		accountA.deposit("Først entry", new BigDecimal(10));
		Thread.sleep(1000);
		accountA.deposit("Andet entry", new BigDecimal(20));
		
		getEntityManager().flush();
		
		List<Entry> entries = accountA.getEntries();
		// Newst first
		assertEquals(new BigDecimal(10), entries.get(1).getAmount());
		assertEquals(new BigDecimal(20), entries.get(0).getAmount());
	}

	public void testEntriesWithoutFlush() throws Exception {
		accountA.deposit("Først entry", new BigDecimal(10));
		Thread.sleep(1000);
		accountA.deposit("Andet entry", new BigDecimal(20));
		
		List<Entry> entries = accountA.getEntries();
		// Newst first
		assertEquals(new BigDecimal(10), entries.get(1).getAmount());
		assertEquals(new BigDecimal(20), entries.get(0).getAmount());
	}
}
