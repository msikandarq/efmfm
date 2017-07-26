package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the eFmFmEscortCheckIn database table.
 * 
 */
@Entity
@Table(name="eFmFmEscortCheckIn")
public class EFmFmEscortCheckInPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="EscortCheckInId", length=10)
	private int escortCheckInId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EscortCheckInTime", length=30)
	private Date escortCheckInTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EscortCheckOutTime", length=30)
	private Date escortCheckOutTime;
	
	@Column(name="Status", length=10)
	private String status;
	
	
	//bi-directional many-to-one association to EFmFmDriverMaster
	@ManyToOne
	@JoinColumn(name="EscortId")
	private EFmFmEscortMasterPO eFmFmEscortMaster;
	
	//bi-directional many-to-one association to EFmFmAssignRoute
	@OneToMany(mappedBy="eFmFmEscortCheckIn")
	private List<EFmFmAssignRoutePO> efmFmAssignRoutes;


	
	public EFmFmEscortCheckInPO() {
	}
	
	public int getEscortCheckInId() {
		return escortCheckInId;
	}

	public void setEscortCheckInId(int escortCheckInId) {
		this.escortCheckInId = escortCheckInId;
	}

	public Date getEscortCheckInTime() {
		return escortCheckInTime;
	}

	public void setEscortCheckInTime(Date escortCheckInTime) {
		this.escortCheckInTime = escortCheckInTime;
	}

	public Date getEscortCheckOutTime() {
		return escortCheckOutTime;
	}

	public void setEscortCheckOutTime(Date escortCheckOutTime) {
		this.escortCheckOutTime = escortCheckOutTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public EFmFmEscortMasterPO geteFmFmEscortMaster() {
		return eFmFmEscortMaster;
	}


	public void seteFmFmEscortMaster(EFmFmEscortMasterPO eFmFmEscortMaster) {
		this.eFmFmEscortMaster = eFmFmEscortMaster;
	}

	public List<EFmFmAssignRoutePO> getEfmFmAssignRoutes() {
		return efmFmAssignRoutes;
	}

	public void setEfmFmAssignRoutes(List<EFmFmAssignRoutePO> efmFmAssignRoutes) {
		this.efmFmAssignRoutes = efmFmAssignRoutes;
	}


}