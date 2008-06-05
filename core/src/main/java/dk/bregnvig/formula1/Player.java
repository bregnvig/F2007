package dk.bregnvig.formula1;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.dao.impl.PersistentRole;

@Entity
@Table(name="player")
public class Player {
	
	private Long id;
	
	private String playername;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String smsNumber;
	private boolean wbcParticipant;
	private Account account = new Account();
	
	private Set<PersistentRole> roles = new HashSet<PersistentRole>();
	
	public Player() {
	}
	
	public Player(PlayerRole ... roles) {
		for (PlayerRole role : roles) {
			this.roles.add(new PersistentRole(role));
		}
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length=40)
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String email) {
		this.emailAddress = email;
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

	@Column(length=20, nullable=false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	
	@Transient
	public Set<PlayerRole> getRoles() {
		Set<PlayerRole> roles = new HashSet<PlayerRole>(5);
		for (PersistentRole role : this.roles) {
			roles.add(role.getRole());
		}
		return roles;
	}
	
	/**
	 * Returns true if the player has the requested role
	 * @param role
	 * @return
	 */
	@Transient
	public boolean isPlayerInRole(PlayerRole role) {
		return getRoles().contains(role);
	}
	
	/**
	 * Returns true if player has acces to one or more for the given roles
	 * @param roles
	 * @return
	 */
	@Transient
	public boolean isPlayerInRoles(PlayerRole[] roles) {
		for (PlayerRole role : roles) {
			if (isPlayerInRole(role)) {
				return true;
			}
		}
		return false;
	}
	
	// Not visible outside the package. Still works with JPA
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="player_id")
	Set<PersistentRole> getPersistentRoles() {
		return this.roles;
	}
	
	public void setPersistentRoles(Set<PersistentRole> roles) {
		this.roles = roles;
	}
	
	@Column(length=8)
	public String getSms() {
		return smsNumber;
	}
	public void setSms(String sms) {
		this.smsNumber = sms;
	}
	
	@OneToOne(optional = false, cascade= {CascadeType.ALL})
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
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

	@Column(nullable = false)
	public boolean isWbcParticipant() {
		return wbcParticipant;
	}

	public void setWbcParticipant(boolean wbcMember) {
		this.wbcParticipant = wbcMember;
	}
}
