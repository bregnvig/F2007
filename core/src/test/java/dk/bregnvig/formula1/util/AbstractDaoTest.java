package dk.bregnvig.formula1.util;

import java.util.Calendar;

import javax.persistence.EntityManager;

import org.springframework.test.jpa.AbstractJpaTests;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.Season;



public abstract class AbstractDaoTest extends AbstractJpaTests{
	
	protected Driver kimi;
	protected Driver massa;
	protected Driver hamilton;
	protected Driver alonso;
	protected Driver heidfeld;
	protected Driver kubica;
	protected Driver button;
	
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
		getEntityManager().persist(alonso = helper.getDriver(1, "Wanker Alonso"));
		getEntityManager().persist(heidfeld = helper.getDriver(7, "Nick Heidfeld"));
		getEntityManager().persist(kubica = helper.getDriver(8, "Robert Kubica"));
		getEntityManager().persist(button = helper.getDriver(19, "Jenson Button"));
		
		getEntityManager().persist(flb = helper.getPlayer("flb"));
		getEntityManager().persist(mba = helper.getPlayer("mba"));
		getEntityManager().persist(ttp = helper.getPlayer("ttp"));
		getEntityManager().persist(bookie = helper.getPlayer("bookie"));

		season = new Season();
		season.setName("F1 2007");
		season.addDriver(kimi);
		season.addDriver(massa);
		season.addDriver(hamilton);
		season.addDriver(alonso);
		season.addDriver(heidfeld);
		season.addDriver(kubica);
		
		season.addPlayer(flb);
		season.addPlayer(mba);
		season.addPlayer(ttp);
		
		monza = new Race();
		monza.setSelectedDriver(kimi);
		monza.setName("Monza");
		monza.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.MINUTE, 2);
		monza.setClose(close);
		
		spa = new Race();
		spa.setSelectedDriver(massa);
		spa.setName("Spa");
		Calendar begin = Calendar.getInstance();
		begin.add(Calendar.MINUTE, 5);
		spa.setOpen(begin);
		Calendar close2 = Calendar.getInstance();
		close2.add(Calendar.MINUTE, 7);
		spa.setClose(close2);
		
		spa.initialized();
		monza.initialized();
		season.addRace(monza);
		season.addRace(spa);
		
		flbMonzaBid = helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 10000);
		mbaMonzaBid = helper.getBid(mba, massa, kimi, hamilton, alonso, kubica, heidfeld, 2, 2, 20000);
		ttpMonzaBid = helper.getBid(ttp, heidfeld, kubica, alonso, hamilton, kimi, massa, 3, 3, 20000);
		flbSpaBid = helper.getBid(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, 1, 1, 10000);
		mbaSpaBid = helper.getBid(mba, massa, kimi, hamilton, alonso, kubica, heidfeld, 2, 2, 20000);
		ttpSpaBid = helper.getBid(ttp, heidfeld, kubica, alonso, hamilton, kimi, massa, 3, 3, 20000);
		raceResultMonza = helper.getRaceResult(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, button, 1, 1, 10000);
		raceResultSpa = helper.getRaceResult(flb, kimi, massa, hamilton, alonso, heidfeld, kubica, button, 1, 1, 10000);

		getEntityManager().persist(season);
	}
	
	

}
