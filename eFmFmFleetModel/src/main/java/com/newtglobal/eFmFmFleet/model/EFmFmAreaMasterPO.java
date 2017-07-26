package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the eFmFm_Area_Master database table.
 * 
 */
@Entity
@Table(name="eFmFmAreaMaster")
public class EFmFmAreaMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AreaId", length=15)
	private int areaId;
	
	@Column(name="AreaName", length=255)
	private String areaName;
	
	
	@Column(name="AreaWiseDistance", length=50)
	private long areaWiseDistance;


	@Column(name="AreaDescription", length=255)
	private String areaDescription;

	
	//bi-directional many-to-one association to EFmFm_Client_Route_Area_Mapping
	@OneToMany(mappedBy="efmFmAreaMaster")
	private List<EFmFmRouteAreaMappingPO> eFmFmRouteAreaMapping;
	
	public EFmFmAreaMasterPO() {
	}



	public int getAreaId() {
		return areaId;
	}



	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}



	public String getAreaName() {
		return areaName;
	}



	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}



	public String getAreaDescription() {
		return areaDescription;
	}



	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}



	public List<EFmFmRouteAreaMappingPO> geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}



	public void seteFmFmRouteAreaMapping(
			List<EFmFmRouteAreaMappingPO> eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}



	public long getAreaWiseDistance() {
		return areaWiseDistance;
	}



	public void setAreaWiseDistance(long areaWiseDistance) {
		this.areaWiseDistance = areaWiseDistance;
	}

	
}