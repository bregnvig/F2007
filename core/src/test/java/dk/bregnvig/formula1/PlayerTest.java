package dk.bregnvig.formula1;

import javax.persistence.PersistenceException;

import dk.bregnvig.formula1.util.AbstractDaoTest;

public class PlayerTest extends AbstractDaoTest {
	
	public void testNormalPlayerRole() {
		assertEquals(3, flb.getRoles().size());
		assertEquals(2, ttp.getRoles().size());
		assertEquals(1, mba.getRoles().size());
	}
	
	public void testPlayerInRole() {
		assertTrue(flb.isPlayerInRole(PlayerRole.PLAYER));
		assertTrue(flb.isPlayerInRole(PlayerRole.PLAYER_ADMIN));
		assertTrue(flb.isPlayerInRole(PlayerRole.ACCOUNT_ADMIN));
		assertFalse(flb.isPlayerInRole(null));

		assertTrue(ttp.isPlayerInRole(PlayerRole.PLAYER));
		assertTrue(ttp.isPlayerInRole(PlayerRole.PLAYER_ADMIN));
		assertFalse(ttp.isPlayerInRole(PlayerRole.ACCOUNT_ADMIN));
		assertFalse(ttp.isPlayerInRole(null));

		assertTrue(mba.isPlayerInRole(PlayerRole.PLAYER));
		assertFalse(mba.isPlayerInRole(PlayerRole.PLAYER_ADMIN));
		assertFalse(mba.isPlayerInRole(PlayerRole.ACCOUNT_ADMIN));
		assertFalse(mba.isPlayerInRole(null));
	}

	public void testPlayerInRoles() {
		PlayerRole[] roles = {PlayerRole.ACCOUNT_ADMIN, PlayerRole.PLAYER_ADMIN};
		assertTrue(flb.isPlayerInRoles(roles));
		assertTrue(ttp.isPlayerInRoles(roles));
		assertFalse(mba.isPlayerInRoles(roles));
	}

	public void testPassword() {
		
		flb.setPassword(null);
		
		try {
			getEntityManager().merge(flb);
			getEntityManager().flush();
			fail("Should have failed. Password is null");
		} catch (PersistenceException expected) {
			
		}
	}
}
