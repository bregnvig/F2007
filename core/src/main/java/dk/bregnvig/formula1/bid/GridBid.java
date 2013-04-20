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
@Table(name="bid_grid")
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="bid")
public class GridBid extends AbstractBid {
	
	private Driver position1;
	private Driver position2;
	private Driver position3;
	private Driver position4;
	private Driver position5;
	private Driver position6;
	private int pointsPosition1;
	private int pointsPosition2;
	private int pointsPosition3;
	private int pointsPosition4;
	private int pointsPosition5;
	private int pointsPosition6;
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
	
	@ManyToOne(optional=false)
	public Driver getPosition4() {
		return position4;
	}
	public void setPosition4(Driver position4) {
		this.position4 = position4;
	}
	
	@ManyToOne(optional=false)
	public Driver getPosition5() {
		return position5;
	}
	public void setPosition5(Driver position5) {
		this.position5 = position5;
	}
	
	@ManyToOne(optional=false)
	public Driver getPosition6() {
		return position6;
	}
	public void setPosition6(Driver position6) {
		this.position6 = position6;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(position1)
			.append(position2)
			.append(position3)
			.append(position4)
			.append(position5)
			.append(position6)
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
		GridBid other = (GridBid) obj;
		
		return new EqualsBuilder()
			.append(position1, other.position1)
			.append(position2, other.position2)
			.append(position3, other.position3)
			.append(position4, other.position4)
			.append(position5, other.position5)
			.append(position6, other.position6)
			.isEquals();
			
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("Position 1", position1)
			.append("Position 2", position2)
			.append("Position 3", position3)
			.append("Position 4", position4)
			.append("Position 5", position5)
			.append("Position 6", position6)
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

	public int getPointsPosition4() {
		return pointsPosition4;
	}

	public void setPointsPosition4(int pointsPosition4) {
		this.pointsPosition4 = pointsPosition4;
	}

	public int getPointsPosition5() {
		return pointsPosition5;
	}

	public void setPointsPosition5(int pointsPosition5) {
		this.pointsPosition5 = pointsPosition5;
	}

	public int getPointsPosition6() {
		return pointsPosition6;
	}

	public void setPointsPosition6(int pointsPosition6) {
		this.pointsPosition6 = pointsPosition6;
	}

	@Override
	@Transient
	public int getPoints() {
		return pointsPosition1+pointsPosition2+pointsPosition3+pointsPosition4+pointsPosition5+pointsPosition6;
	}	
}
