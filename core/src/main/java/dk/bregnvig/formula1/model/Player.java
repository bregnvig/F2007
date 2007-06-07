package dk.bregnvig.formula1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="player")
public class Player {
	
	private Long id;
	
	private String playername;
	private String firstName;
	private String lastName;
	private String email;
	private String sms;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=40)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(length=25, nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(length=25, nullable=false)
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(length=15, nullable=false, unique=true)
	public String getPlayername() {
		return playername;
	}
	public void setPlayername(String playername) {
		this.playername = playername;
	}
	
	@Column(length=8)
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Player == false) {
			return false;
		}
		
		Player other = (Player) obj;
		
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(playername, other.playername)
			.isEquals();
	}
	@Override
	public int hashCode() {
		return playername.hashCode();
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append(playername)
			.toString();
	}	
}
