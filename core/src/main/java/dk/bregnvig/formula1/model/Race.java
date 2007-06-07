package dk.bregnvig.formula1.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Contains a single
 * 
 * @author bregnvig
 * 
 */
@Entity
@Table(name="race")
public class Race {

	private Long id;

	private String name;
	private Calendar open;
	private Calendar close;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=25, nullable=false)
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getClose() {
		return close;
	}

	public void setClose(Calendar close) {
		validateDates(this.open, close);
		this.close = close;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Calendar getOpen() {
		return open;
	}

	public void setOpen(Calendar open) {
		validateDates(open, this.close);
		this.open = open;
		
	}

	private void validateDates(Calendar open, Calendar close) {
		if (open == null || close == null) {
			return;
		}
		if (open.after(close)) {
			throw new IllegalStateException("Race " + name + " starts after is ends");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Race == false) {
			return false;
		}
		
		Race other = (Race) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(name, other.name)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "Race: " + name;
	}
	
}
