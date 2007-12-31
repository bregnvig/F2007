package dk.bregnvig.formula1.server.mapping.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.account.Account.Entry;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.account.ClientAccountEntry;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;

public class ObjectFactoryImpl implements ObjectFactory{

	public ClientRace create(Race source) {
		ClientRace clientRace = new ClientRace();
		BeanUtils.copyProperties(source, clientRace);
		clientRace.setOpenDate(source.getOpen().getTime());
		return clientRace;
	}

	public ClientPlayer create(Player player) {
		ClientPlayer clientPlayer = new ClientPlayer();
		BeanUtils.copyProperties(player, clientPlayer);
		return clientPlayer;
	}
	
	public ClientAccount create(Account account) {
		ClientAccount clientAccount = new ClientAccount();
		clientAccount.setBalance(account.getBalance().intValue());

		List<ClientAccountEntry> entries = new ArrayList<ClientAccountEntry>(account.getEntries().size()); 
		for (Entry entry : account.getEntries()) {
			entries.add(create(entry));
		}
		clientAccount.setEntries(entries);
		return clientAccount;
	}
	
	public ClientSeason create(Season season) {
		ClientSeason clientSeason = new ClientSeason();
		clientSeason.setName(season.getName());
		
		clientSeason.setRaces(getClientRaces(season));
		clientSeason.setPlayers(getClientPlayers(season));
		
		return clientSeason;
	}

	private List<ClientPlayer> getClientPlayers(Season season) {
		Set<Player> players = season.getPlayers();
		
		List<ClientPlayer> clientPlayers = new ArrayList<ClientPlayer>(players.size());
		for (Player player : players) {
			clientPlayers.add(create(player));
		}
		return clientPlayers;
	}

	@SuppressWarnings("unchecked")
	private List<ClientRace> getClientRaces(Season season) {
		Set<Race> races = season.getRaces();
		
		List<ClientRace> clientRaces = new ArrayList<ClientRace>(races.size());
		for (Race race : races) {
			clientRaces.add(create(race));
		}
		Collections.sort(clientRaces, new ClientRace.OpensComparator());
		return clientRaces;
	}
	
	private ClientAccountEntry create(Account.Entry entry) {
		ClientAccountEntry clientEntry = new ClientAccountEntry();
		clientEntry.setAmount(entry.getAmount().intValue());
		clientEntry.setDate(entry.getDate().getTime());
		clientEntry.setMessage(entry.getMessage());
		return clientEntry;
	}

	public void map(ClientPlayer source, Player target) {
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmailAddress(source.getEmailAddress());
		target.setSms(source.getSms());
	}

}
