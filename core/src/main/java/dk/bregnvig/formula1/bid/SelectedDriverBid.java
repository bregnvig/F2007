package dk.bregnvig.formula1.bid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="bid_select_driver")
public class SelectedDriverBid extends AbstractBid {
	

	private int startPosition;
	private int endPosition;
	private int pointsStartPosition;
	private int pointsEndPosition;
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

	@Column(nullable=false)
	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	@Column(nullable=false)
	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}	

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("Starts postion", startPosition)
			.append("End position", endPosition)
			.toString();
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
		
		SelectedDriverBid other = (SelectedDriverBid) obj;
		
		return new EqualsBuilder()
			.append(startPosition, other.startPosition)
			.append(endPosition, other.endPosition)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(startPosition)
			.append(endPosition)
			.toHashCode();
	}

	public int getPointsEndPosition() {
		return pointsEndPosition;
	}

	public void setPointsEndPosition(int pointsEndPosition) {
		this.pointsEndPosition = pointsEndPosition;
	}

	public int getPointsStartPosition() {
		return pointsStartPosition;
	}

	public void setPointsStartPosition(int pointsStartPosition) {
		this.pointsStartPosition = pointsStartPosition;
	}

	@Override
	@Transient
	public int getPoints() {
		return pointsStartPosition+pointsEndPosition;
	}
}
