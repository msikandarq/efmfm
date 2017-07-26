package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the eFmFmRoleMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmRoleMaster")
public class EFmFmRoleMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="RoleId", length=10)
	private int roleId;

	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;

	@Column(name="Role", length=50)
	private String role;

	@Column(name="Status", length=10)
	private String status;

	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	//bi-directional many-to-one association to EFmFmClientUserRole
	@OneToMany(mappedBy="efmFmRoleMaster")
	private List<EFmFmClientUserRolePO> efmFmClientUserRoles;

   
	public EFmFmRoleMasterPO() {
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<EFmFmClientUserRolePO> getEfmFmClientUserRoles() {
		return this.efmFmClientUserRoles;
	}

	public void setEfmFmClientUserRoles(List<EFmFmClientUserRolePO> efmFmClientUserRoles) {
		this.efmFmClientUserRoles = efmFmClientUserRoles;
	}

	public EFmFmClientUserRolePO addEfmFmClientUserRole(EFmFmClientUserRolePO efmFmClientUserRole) {
		getEfmFmClientUserRoles().add(efmFmClientUserRole);
		efmFmClientUserRole.setEfmFmRoleMaster(this);

		return efmFmClientUserRole;
	}

	public EFmFmClientUserRolePO removeEfmFmClientUserRole(EFmFmClientUserRolePO efmFmClientUserRole) {
		getEfmFmClientUserRoles().remove(efmFmClientUserRole);
		efmFmClientUserRole.setEfmFmRoleMaster(null);

		return efmFmClientUserRole;
	}

}
