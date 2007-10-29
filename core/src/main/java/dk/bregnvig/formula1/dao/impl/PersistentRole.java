package dk.bregnvig.formula1.dao.impl;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import dk.bregnvig.formula1.PlayerRole;

@Entity
public class PersistentRole {
	
	private Long id;
	private PlayerRole role;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Enumerated
	public PlayerRole getRole() {
		return role;
	}
	public void setRole(PlayerRole role) {
		this.role = role;
	}
}
