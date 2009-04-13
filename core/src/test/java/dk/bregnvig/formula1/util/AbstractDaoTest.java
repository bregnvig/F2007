package dk.bregnvig.formula1.util;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.springframework.test.jpa.AbstractJpaTests;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.service.EventService;



public abstract class AbstractDaoTest extends AbstractJpaTests{
	
	protected Driver driver1;
	protected Driver driver2;
	protected Driver driver3;
	protected Driver driver4;
	protected Driver driver5;
	protected Driver driver6;
	protected Driver driver7;
	protected Driver driver8;
	protected Driver driver9;
	protected Driver driver10;
	protected Driver driver11;
	protected Driver driver12;
	protected Driver driver14;
	protected Driver driver15;
	protected Driver driver16;
	protected Driver driver17;
	protected Driver driver20;
	protected Driver driver21;
	protected Driver driver22;
	protected Driver driver23;
	
	protected Player flb;
	protected Player mba;
	protected Player ttp;
	protected Player bookie;
	
	protected Race monza;
	protected Race spa;
	
	protected Season season;
	
	protected Bid flbMonzaBid;
	protected Bid mbaMonzaBid;
	protected Bid ttpMonzaBid;
	protected Bid flbSpaBid;
	protected Bid mbaSpaBid;
	protected Bid ttpSpaBid;
	protected RaceResult raceResultMonza;
	protected RaceResult raceResultSpa;
	
	protected EventService eventService;
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	protected CreatorHelper helper = new CreatorHelper();
	

    protected String[] getConfigLocations() {
        return SpringStarter.getConfigLocations();//new String[] {"daoITestContext.xml","file:WebContent/WEB-INF/contexts/data/daoContext.xml"};
    }

	public EntityManager getEntityManager(){
		return this.sharedEntityManager;
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		
		
		getEntityManager().persist(driver1 = helper.getDriver(1, "Driver 1"));
		getEntityManager().persist(driver2 = helper.getDriver(2, "Driver 2"));
		getEntityManager().persist(driver3 = helper.getDriver(3, "Driver 3"));
		getEntityManager().persist(driver4 = helper.getDriver(4, "Driver 4"));
		getEntityManager().persist(driver5 = helper.getDriver(5, "Driver 5"));
		getEntityManager().persist(driver6 = helper.getDriver(6, "Driver 6"));
		getEntityManager().persist(driver7 = helper.getDriver(7, "Driver 7"));
		getEntityManager().persist(driver8 = helper.getDriver(8, "Driver 8"));
		getEntityManager().persist(driver9 = helper.getDriver(9, "Driver 9"));
		getEntityManager().persist(driver10 = helper.getDriver(10, "Driver 10"));
		getEntityManager().persist(driver11 = helper.getDriver(11, "Driver 11"));
		getEntityManager().persist(driver12 = helper.getDriver(12, "Driver 12"));
		getEntityManager().persist(driver14 = helper.getDriver(14, "Driver 14"));
		getEntityManager().persist(driver15 = helper.getDriver(15, "Driver 15"));
		getEntityManager().persist(driver16 = helper.getDriver(16, "Driver 16"));
		getEntityManager().persist(driver17 = helper.getDriver(17, "Driver 17"));
		getEntityManager().persist(driver20 = helper.getDriver(20, "Driver 20"));
		getEntityManager().persist(driver21 = helper.getDriver(21, "Driver 21"));
		getEntityManager().persist(driver22 = helper.getDriver(22, "Driver 22"));
		getEntityManager().persist(driver23 = helper.getDriver(23, "Driver 23"));
		
		getEntityManager().persist(flb = helper.getPlayer("flb"));
		getEntityManager().persist(mba = helper.getPlayer("mba"));
		getEntityManager().persist(ttp = helper.getPlayer("ttp"));
		getEntityManager().persist(bookie = helper.getPlayer("bookie"));

		flb.getAccount().deposit("Start money", new BigDecimal(100));
		mba.getAccount().deposit("Start money", new BigDecimal(100));
		ttp.getAccount().deposit("Start money", new BigDecimal(100));
		
		season = new Season();
		season.setName("F1 2007");
		season.addDriver(driver1);
		season.addDriver(driver2);
		season.addDriver(driver3);
		season.addDriver(driver4);
		season.addDriver(driver5);
		season.addDriver(driver6);
		season.addDriver(driver7);
		season.addDriver(driver8);
		season.addDriver(driver9);
		season.addDriver(driver10);
		season.addDriver(driver11);
		season.addDriver(driver12);
		season.addDriver(driver14);
		season.addDriver(driver15);
		season.addDriver(driver16);
		season.addDriver(driver17);
		season.addDriver(driver20);
		season.addDriver(driver21);
		season.addDriver(driver22);
		season.addDriver(driver23);
		
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		
		monza = new Race();
		monza.setSelectedDriver(driver1);
		monza.setName("Monza");
		Calendar begin = Calendar.getInstance();
		begin.add(Calendar.SECOND, -1);
		monza.setOpen(begin);
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MINUTE, 2);
		monza.setClose(close);
		
		spa = new Race();
		spa.setSelectedDriver(driver2);
		spa.setName("Spa");
		begin = Calendar.getInstance();
		begin.add(Calendar.MINUTE, 5);
		spa.setOpen(begin);
		Calendar close2 = Calendar.getInstance();
		close2.add(Calendar.MINUTE, 7);
		spa.setClose(close2);
		
		season.addRace(monza);
		season.addRace(spa);
		
		flbMonzaBid = helper.getBid(flb, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 10000);
		mbaMonzaBid = helper.getBid(mba, driver2, driver1, driver3, driver4, driver6, driver5, 2, 2, 20000);
		ttpMonzaBid = helper.getBid(ttp, driver5, driver6, driver4, driver3, driver1, driver2, 3, 3, 20000);
		flbSpaBid = helper.getBid(flb, driver1, driver2, driver3, driver4, driver5, driver6, 1, 1, 10000);
		mbaSpaBid = helper.getBid(mba, driver2, driver1, driver3, driver4, driver6, driver5, 2, 2, 20000);
		ttpSpaBid = helper.getBid(ttp, driver5, driver6, driver4, driver3, driver1, driver2, 3, 3, 20000);
		raceResultMonza = helper.getRaceResult(flb, driver1, driver2, driver3, driver4, driver5, driver6, driver7, 1, 1, 10000);
		raceResultSpa = helper.getRaceResult(flb, driver1, driver2, driver3, driver4, driver5, driver6, driver7, 1, 1, 10000);

		getEntityManager().persist(season);
		eventService.initialize();
	}
	
	

}
