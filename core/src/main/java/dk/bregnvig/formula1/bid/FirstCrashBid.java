package dk.bregnvig.formula1.bid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import dk.bregnvig.formula1.Driver;

@Entity
@Table(name="bid_first_crash")
public class FirstCrashBid extends AbstractBid {
	
	private Driver crash1;
	private int pointsCrash1;
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
	public Driver getCrash1() {
		return crash1;
	}
		
	public void setCrash1(Driver firstCrash) {
		this.crash1 = firstCrash;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("Driver", crash1)
			.toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(crash1)
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
		FirstCrashBid other = (FirstCrashBid) obj;
		
		return new EqualsBuilder()
			.append(crash1, other.crash1)
			.isEquals();
	}

	@Transient
	public int getPoints() {
		return pointsCrash1;
	}

	public int getPointsCrash1() {
		return pointsCrash1;
	}

	public void setPointsCrash1(int pointsCrash1) {
		this.pointsCrash1 = pointsCrash1;
	}
	
	
}
