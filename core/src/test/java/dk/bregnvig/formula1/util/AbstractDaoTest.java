package dk.bregnvig.formula1.util;

import javax.persistence.EntityManager;

import org.springframework.test.jpa.AbstractJpaTests;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;



public abstract class AbstractDaoTest extends AbstractJpaTests{
	
	protected Driver kimi;
	protected Driver massa;
	protected Driver hamilton;
	
	protected Player flb;
	protected Player mba;
	protected Player ttp;
	
	protected CreatorHelper helper = new CreatorHelper();
	

    protected String[] getConfigLocations() {
        return SpringStarter.getConfigLocations();//new String[] {"daoITestContext.xml","file:WebContent/WEB-INF/contexts/data/daoContext.xml"};
    }

	public EntityManager getEntityManager(){
		return this.sharedEntityManager;
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		getEntityManager().persist(kimi = helper.getDriver(6, "Kimi Raikonen"));
		getEntityManager().persist(massa = helper.getDriver(5, "Felipe Massa"));
		getEntityManager().persist(hamilton = helper.getDriver(2, "Lewis Hamilton"));
		
		getEntityManager().persist(flb = helper.getPlayer("flb"));
		getEntityManager().persist(mba = helper.getPlayer("mba"));
		getEntityManager().persist(ttp = helper.getPlayer("ttp"));
	}
	
	

}
