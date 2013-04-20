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
	
	private Driver crash2;
	private Driver crash3;

	@ManyToOne(optional=true)
	public Driver getCrash2() {
		return crash2;
	}
		
	public void setCrash2(Driver crash) {
		this.crash2 = crash;
	}
		
	@ManyToOne(optional=true)
	public Driver getCrash3() {
		return crash3;
	}
		
	public void setCrash3(Driver crash) {
		this.crash3 = crash;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append(crash2)
			.append(crash3)
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
			.append(crash2, other.crash2)
			.append(crash3, other.crash3)
			.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append(crash2)
			.append(crash3)
			.toString();
	}
}
