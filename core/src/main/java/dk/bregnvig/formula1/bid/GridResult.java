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
public class GridResult extends GridBid {

	private Driver position7;

	@ManyToOne
	public Driver getPosition7() {
		return position7;
	}

	public void setPosition7(Driver position7) {
		this.position7 = position7;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.appendSuper(super.hashCode())
			.append(position7)
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
		GridResult other = (GridResult) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(position7, other.position7)
			.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append(position7)
			.toString();
	}
	
	
	
}
