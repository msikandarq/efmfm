package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@Entity
@Table(name="persistent_logins")
public class PersistentLoginPO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="last_used" ,nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	Date lastUsed;
	
	@Column(name="username" ,nullable = false, length=50)
	String userName;
	
	@Column(name="token" ,nullable = false, length=50)
	String token;
	
	@Column(name="ipaddress", length=50)
	String ipAddress;
	
	@Id
	@Column(name="series" ,nullable = false, length=50)
	String series;
	


	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}
	

}