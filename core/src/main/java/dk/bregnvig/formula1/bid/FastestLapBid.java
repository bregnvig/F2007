package dk.bregnvig.formula1.bid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import dk.bregnvig.formula1.Driver;

@Entity
@Table(name="bid_fastest_lap")
public class FastestLapBid extends AbstractBid {
	
	private Driver driver;
	private int points;
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional=false)
	public Driver getDriver() {
		return driver;
	}
		
	public void setDriver(Driver position1) {
		this.driver = position1;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("Driver", driver)
			.toString();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(driver)
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FastestLapBid other = (FastestLapBid) obj;
		
		return new EqualsBuilder()
			.append(driver, other.driver)
			.isEquals();
			
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}	
}
