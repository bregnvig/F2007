package dk.bregnvig.formula1.util;

import javax.persistence.EntityManager;

import org.springframework.test.jpa.AbstractJpaTests;



public abstract class AbstractDaoTest extends AbstractJpaTests{

    protected String[] getConfigLocations() {
        return SpringStarter.getConfigLocations();//new String[] {"daoITestContext.xml","file:WebContent/WEB-INF/contexts/data/daoContext.xml"};
    }

	public EntityManager getEntityManager(){
		return this.sharedEntityManager;
	}
}
