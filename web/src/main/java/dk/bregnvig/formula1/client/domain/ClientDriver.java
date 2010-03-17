package dk.bregnvig.formula1.client.domain;

import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientDriver implements IsSerializable {

	private Long id;
	private int number;
	private String name;
	private boolean active = true;
	private boolean partOfSeason = false;
	private int points;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public static class NumberComparator implements Comparator<ClientDriver>, IsSerializable {

		public int compare(ClientDriver driver0, ClientDriver driver1) {
			return new Integer(driver0.getNumber()).compareTo(new Integer(driver1.getNumber()));
		}
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final ClientDriver other = (ClientDriver) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	public boolean isPartOfSeason() {
		return partOfSeason;
	}
	public void setPartOfSeason(boolean partOfSeason) {
		this.partOfSeason = partOfSeason;
	}

	

}
