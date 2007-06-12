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
public class PodiumResult extends PodiumBid {
	
	private Driver position4;

	@ManyToOne(optional=true)
	public Driver getPosition4() {
		return position4;
	}
		
	public void setPosition4(Driver position4) {
		this.position4 = position4;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append(position4)
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
		PodiumResult other = (PodiumResult) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(position4, other.position4)
			.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append(position4)
			.toString();
	}
}
