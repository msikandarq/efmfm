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
 * The persistent class for the EFmFmUserPasswordPO database table.
 * 
 */
@Entity
@Table(name = "eFmFmUserPassword")
public class EFmFmUserPasswordPO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PasswordId", length = 10)
    private int passwordId;
    
    
    @Column(name = "CreatedBy", length = 50)
    private String createdBy;
    
    
    @Column(name = "password", length = 250)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreationTime", length = 50)
    private Date creationTime;
    
  //bi-directional many-to-one association to eFmFmUserMaster
    @ManyToOne
    @JoinColumn(name="UserId")
    private EFmFmUserMasterPO efmFmUserMaster;
    
  //bi-directional many-to-one association to EFmFmClientBranchPO
    @ManyToOne
    @JoinColumn(name="BranchId")
    private EFmFmClientBranchPO eFmFmClientBranchPO;
    

    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
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

    public EFmFmUserMasterPO getEfmFmUserMaster() {
        return efmFmUserMaster;
    }

    public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
        this.efmFmUserMaster = efmFmUserMaster;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EFmFmClientBranchPO geteFmFmClientBranchPO() {
        return eFmFmClientBranchPO;
    }

    public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
        this.eFmFmClientBranchPO = eFmFmClientBranchPO;
    } 
    
    
}
