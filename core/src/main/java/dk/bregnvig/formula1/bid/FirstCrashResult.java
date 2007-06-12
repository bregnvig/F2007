package dk.bregnvig.formula1.bid;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import dk.bregnvig.formula1.Driver;

@Entity
@DiscriminatorValue(value="result")
public class FirstCrashResult extends FirstCrashBid {
	
	private Driver secondCrash;
	private Driver thirdCrash;

	@ManyToOne(optional=true)
	public Driver getSecondCrash() {
		return secondCrash;
	}
		
	public void setSecondCrash(Driver crash) {
		this.secondCrash = crash;
	}
		
	@ManyToOne(optional=true)
	public Driver getThirdCrash() {
		return thirdCrash;
	}
		
	public void setThirdCrash(Driver crash) {
		this.thirdCrash = crash;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append(secondCrash)
			.append(thirdCrash)
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
		FirstCrashResult other = (FirstCrashResult) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(secondCrash, other.secondCrash)
			.append(thirdCrash, other.thirdCrash)
			.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append(secondCrash)
			.append(thirdCrash)
			.toString();
	}
}
