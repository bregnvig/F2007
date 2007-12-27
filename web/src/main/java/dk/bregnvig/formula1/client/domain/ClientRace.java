package dk.bregnvig.formula1.client.domain;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientRace implements IsSerializable{

	private Long id;

	private String name;
	private boolean completed;
	private Date openDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date opens) {
		this.openDate = opens;
	}
	
	public static class OpensComparator implements Comparator, IsSerializable {

		public int compare(Object arg0, Object arg1) {
			ClientRace race0 = (ClientRace) arg0;
			ClientRace race1 = (ClientRace) arg1;
			
			return race0.getOpenDate().compareTo(race1.getOpenDate());
		}
	}
	
}
