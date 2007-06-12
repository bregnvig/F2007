package dk.bregnvig.formula1.bid;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
@Table(name="bid_podium")
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="bid")
public class PodiumBid extends AbstractBid {
	
	private Driver position1;
	private Driver position2;
	private Driver position3;
	private int pointsPosition1;
	private int pointsPosition2;
	private int pointsPosition3;
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
	public Driver getPosition1() {
		return position1;
	}
		
	public void setPosition1(Driver position1) {
		this.position1 = position1;
	}
	
	@ManyToOne(optional=false)
	public Driver getPosition2() {
		return position2;
	}
	public void setPosition2(Driver position2) {
		this.position2 = position2;
	}
	
	@ManyToOne(optional=false)
	public Driver getPosition3() {
		return position3;
	}
	public void setPosition3(Driver position3) {
		this.position3 = position3;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(position1)
			.append(position2)
			.append(position3)
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
		PodiumBid other = (PodiumBid) obj;
		
		return new EqualsBuilder()
			.append(position1, other.position1)
			.append(position2, other.position2)
			.append(position3, other.position3)
			.isEquals();
			
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("Position 1", position1)
			.append("Position 2", position2)
			.append("Position 3", position3)
			.toString();
	}

	public int getPointsPosition1() {
		return pointsPosition1;
	}

	public void setPointsPosition1(int pointsPosition1) {
		this.pointsPosition1 = pointsPosition1;
	}

	public int getPointsPosition2() {
		return pointsPosition2;
	}

	public void setPointsPosition2(int pointsPosition2) {
		this.pointsPosition2 = pointsPosition2;
	}

	public int getPointsPosition3() {
		return pointsPosition3;
	}

	public void setPointsPosition3(int pointsPosition3) {
		this.pointsPosition3 = pointsPosition3;
	}	

	@Override
	@Transient
	public int getPoints() {
		return pointsPosition1+pointsPosition2+pointsPosition3;
	}	
}
