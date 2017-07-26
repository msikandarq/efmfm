package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the eFmFmVehicleCheckIn database table.
 * 
 */
@Entity
@Table(name="eFmFmEscortDocs")
public class EFmFmEscortDocsPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="EscortDocId", length=10)
	private int escortDocId;

	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;

		
	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="DocumentName", length=200)
	private String documentName;
	
	@Column(name="UploadPath", length=200)
	private String uploadpath;	
    
	//bi-directional many-to-one association to EFmFmDriverMaster
	@ManyToOne
	@JoinColumn(name="EscortId")
	private EFmFmEscortMasterPO eFmFmEscortMaster;	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getUploadpath() {
		return uploadpath;
	}

	public void setUploadpath(String uploadpath) {
		this.uploadpath = uploadpath;
	}

	public int getEscortDocId() {
		return escortDocId;
	}

	public void setEscortDocId(int escortDocId) {
		this.escortDocId = escortDocId;
	}

	public EFmFmEscortMasterPO geteFmFmEscortMaster() {
		return eFmFmEscortMaster;
	}

	public void seteFmFmEscortMaster(EFmFmEscortMasterPO eFmFmEscortMaster) {
		this.eFmFmEscortMaster = eFmFmEscortMaster;
	}

	

	
}