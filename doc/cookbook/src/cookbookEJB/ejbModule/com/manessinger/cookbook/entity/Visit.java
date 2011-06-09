package com.manessinger.cookbook.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.azzyzt.jee.runtime.annotation.CreateTimestamp;
import org.azzyzt.jee.runtime.annotation.CreateUser;
import org.azzyzt.jee.runtime.annotation.Internal;
import org.azzyzt.jee.runtime.annotation.ModificationTimestamp;
import org.azzyzt.jee.runtime.annotation.ModificationUser;
import org.azzyzt.jee.runtime.entity.EntityBase;

@Entity
@Table(name="visit")
public class Visit extends EntityBase<VisitId> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private VisitId id;
	
	@Column(name="total_number_of_visitors")
	private Long totalNumberOfVisitors;

	@CreateTimestamp(format="yyyy-MM-dd-HHmmss.SSS")
	@Column(name="create_timestamp")
	private String createTimestamp;

	@ModificationTimestamp(format="yyyy-MM-dd-HHmmss.SSS")
	@Column(name="modification_timestamp")
	private String modificationTimestamp;

	@CreateUser
	@Column(name="create_user")
	private String createUser;

	@ModificationUser
	@Column(name="modification_user")
	private String modificationUser;

	@Internal @ManyToOne
	@JoinColumn(name="from_zip_area", insertable=false, updatable=false)
	private Zip fromZipArea;
	
	@Internal @ManyToOne
	@JoinColumn(name="to_city", insertable=false, updatable=false)
	private City toCity;
	
	@Internal @ManyToOne
	@JoinColumn(name="lang_used", insertable=false, updatable=false)
	private Language languageUsedByGuide;
	
	public VisitId getId() {
		if (id == null) {
			return null;
		}
		VisitId result = new VisitId(id.getFromZipArea(), id.getToCity(), id.getLangUsed());
		return result;
	}

	public void setId(VisitId id) {
		if (id == null) {
			return;
		}
		this.id = new VisitId(id.getFromZipArea(), id.getToCity(), id.getLangUsed());
	}

	public Long getTotalNumberOfVisitors() {
		return totalNumberOfVisitors;
	}

	public void setTotalNumberOfVisitors(Long totalNumberOfVisitors) {
		this.totalNumberOfVisitors = totalNumberOfVisitors;
	}

	public String getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getModificationTimestamp() {
		return modificationTimestamp;
	}

	public void setModificationTimestamp(String modificationTimestamp) {
		this.modificationTimestamp = modificationTimestamp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModificationUser() {
		return modificationUser;
	}

	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

	public Zip getFromZipArea() {
		return fromZipArea;
	}

	public void setFromZipArea(Zip fromZipArea) {
		this.fromZipArea = fromZipArea;
	}

	public City getToCity() {
		return toCity;
	}

	public void setToCity(City toCity) {
		this.toCity = toCity;
	}

	public void setLanguageUsedByGuide(Language languageUsedByGuide) {
		this.languageUsedByGuide = languageUsedByGuide;
	}

	public Language getLanguageUsedByGuide() {
		return languageUsedByGuide;
	}
}
