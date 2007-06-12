package dk.bregnvig.formula1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class contains a driver
 * @author 4u603ctpsz9
 *
 */
@Entity
@Table(name="driver")
public class Driver {
	
	private Long id;
	private int number;
	private String name;
	private boolean active = true;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	@Column(length=50, nullable=false)
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false)
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Driver == false) {
			return false;
		}
		
		Driver other = (Driver) obj;
		
		return new EqualsBuilder()
			.append(number, other.number)
			.append(name, other.name)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(number)
			.append(name)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append(number)
			.append(name)
			.toString();
	}

}
