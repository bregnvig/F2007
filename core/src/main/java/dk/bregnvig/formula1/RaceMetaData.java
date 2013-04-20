package dk.bregnvig.formula1;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Configurable;

@Entity(name="race_meta_data")
@Configurable
class RaceMetaData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@Column(name="result_url")
	private String previousResultURL;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Calendar closeTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPreviousResultURL() {
		return previousResultURL;
	}
	public void setPreviousResultURL(String previousResultURL) {
		this.previousResultURL = previousResultURL;
	}
	public Calendar getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Calendar closeTime) {
		this.closeTime = closeTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLastYearPoleTime() {
		
		return 0;
	}
}
