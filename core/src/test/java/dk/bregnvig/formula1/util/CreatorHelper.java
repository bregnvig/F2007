package dk.bregnvig.formula1.util;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.bid.FastestLapBid;
import dk.bregnvig.formula1.bid.FirstCrashBid;
import dk.bregnvig.formula1.bid.FirstCrashResult;
import dk.bregnvig.formula1.bid.GridBid;
import dk.bregnvig.formula1.bid.GridResult;
import dk.bregnvig.formula1.bid.PodiumBid;
import dk.bregnvig.formula1.bid.PodiumResult;
import dk.bregnvig.formula1.bid.SelectedDriverBid;
import dk.bregnvig.formula1.dao.impl.PersistentRole;

public class CreatorHelper {

	public Player getPlayer(String playername) {
		Player player = new Player();
		player.setPlayername(playername);
		player.setFirstName("first"+playername);
		player.setLastName("last"+playername);
		player.setEmailAddress("flemming.bregnvig@gmail.com");
		player.setReminderWanted(true);
		if (playername.equals("mba")) {
			player.setSms("20896003");
		} else {
			player.setSms("28712234");
		}
		player.setPassword("mypassword");
		Set<PersistentRole> roles = new HashSet<PersistentRole>();
		PersistentRole role = new PersistentRole();
		role.setRole(PlayerRole.PLAYER);
		roles.add(role);
		if (playername.equals("flb") || playername.equals("ttp")) {
			PersistentRole adminRole = new PersistentRole();
			adminRole.setRole(PlayerRole.PLAYER_ADMIN);
			roles.add(adminRole);
		}
		if (playername.equals("flb")) {
			PersistentRole accountRole = new PersistentRole();
			accountRole.setRole(PlayerRole.ACCOUNT_ADMIN);
			roles.add(accountRole);
		}
		player.setPersistentRoles(roles);
		return player;
	}
	
	public Driver getDriver(int number, String name) {
		Driver driver = new Driver();
		driver.setName(name);
		driver.setNumber(number);
		return driver;
	}
	
	public Race getRace(String name) {
		Race race = new Race();
		race.setName(name);
		race.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.WEEK_OF_YEAR, 1);
		race.setClose(close);
		return race;
	}
	
	public Bid getBid(Player player, Driver driver1, Driver driver2, Driver driver3, Driver driver4, Driver driver5, Driver driver6, int begin, int end, int polePositionTimeMillis) {
		Bid bid = new Bid();
		bid.setPlayer(player);
		
		bid.setFastestLap(getFastestLapBid(driver1));
		bid.setFirstCrash(getFirstCrashBid(driver6));
		bid.setGrid(getGridBid(driver1, driver2, driver3, driver4, driver5, driver6));
		bid.setPodium(getPodiumBid(driver1, driver2, driver3));
		bid.setSelectedDriver(getSelectedDriverBid(begin, end));
		bid.setPolePositionTimeMillis(polePositionTimeMillis);
		return bid;
	}

	public RaceResult getRaceResult(Player player, Driver driver1, Driver driver2, Driver driver3, Driver driver4, Driver driver5, Driver driver6, Driver driver7, int begin, int end, int polePositionTimeMillis) {
		RaceResult bid = new RaceResult();
		bid.setPlayer(player);
		
		bid.setFastestLap(getFastestLapBid(driver1));
		bid.setFirstCrash(getFirstCrashResult(driver6, driver5, driver4));
		bid.setGrid(getGridResult(driver1, driver2, driver3, driver4, driver5, driver6, driver7));
		bid.setPodium(getPodiumResult(driver1, driver2, driver3, driver4));
		bid.setSelectedDriver(getSelectedDriverBid(begin, end));
		bid.setPolePositionTimeMillis(polePositionTimeMillis);
		return bid;
	}
	
	private PodiumResult getPodiumResult(Driver driver1, Driver driver2, Driver driver3, Driver driver4) {
		PodiumResult result = new PodiumResult();
		result.setPosition1(driver1);
		result.setPosition2(driver2);
		result.setPosition3(driver3);
		result.setPosition4(driver4);
		return result;
	}

	private GridResult getGridResult(Driver driver1, Driver driver2, Driver driver3, Driver driver4, Driver driver5, Driver driver6, Driver driver7) {
		GridResult result = new GridResult();
		result.setPosition1(driver1);
		result.setPosition2(driver2);
		result.setPosition3(driver3);
		result.setPosition4(driver4);
		result.setPosition5(driver5);
		result.setPosition6(driver6);
		result.setPosition7(driver7);
		return result;	
	}

	private FirstCrashResult getFirstCrashResult(Driver driver1, Driver driver2, Driver driver3) {
		FirstCrashResult result = new FirstCrashResult();
		result.setCrash1(driver1);
		result.setCrash2(driver2);
		result.setCrash3(driver3);
		return result;
	}

	private SelectedDriverBid getSelectedDriverBid(int begin, int end) {
		SelectedDriverBid bid = new SelectedDriverBid();
		bid.setStartPosition(begin);
		bid.setEndPosition(end);
		return bid;
	}

	private PodiumBid getPodiumBid(Driver driver1, Driver driver2, Driver driver3) {
		PodiumBid bid = new PodiumBid();
		bid.setPosition1(driver1);
		bid.setPosition2(driver2);
		bid.setPosition3(driver3);
		return bid;
	}

	private GridBid getGridBid(Driver driver1, Driver driver2, Driver driver3, Driver driver4, Driver driver5, Driver driver6) {
		GridBid bid = new GridBid();
		bid.setPosition1(driver1);
		bid.setPosition2(driver2);
		bid.setPosition3(driver3);
		bid.setPosition4(driver4);
		bid.setPosition5(driver5);
		bid.setPosition6(driver6);
		return bid;
	}

	public FastestLapBid getFastestLapBid(Driver driver) {
		FastestLapBid bid = new FastestLapBid();
		bid.setDriver(driver);
		return bid;
	}
	
	public FirstCrashBid getFirstCrashBid(Driver firstCrash) {
		FirstCrashBid bid = new FirstCrashBid();
		bid.setCrash1(firstCrash);
		return bid;
	}
}
