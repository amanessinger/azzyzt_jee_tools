package com.manessinger.cookbook.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.azzyzt.jee.runtime.annotation.CreateTimestamp;
import org.azzyzt.jee.runtime.annotation.CreateUser;
import org.azzyzt.jee.runtime.annotation.Internal;
import org.azzyzt.jee.runtime.annotation.ModificationTimestamp;
import org.azzyzt.jee.runtime.annotation.ModificationUser;
import org.azzyzt.jee.runtime.entity.EntityBase;

import com.manessinger.cookbook.entity.StandardEntityListeners;

@Entity
@Table(name="visit")
@EntityListeners({StandardEntityListeners.class})
public class Visit extends EntityBase<VisitId> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private VisitId id;
	
	@Column(name="number_of_visitors")
	private Long numberOfVisitors;

	@CreateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_timestamp")
	private Calendar createTimestamp;

	@ModificationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modification_timestamp")
	private Calendar modificationTimestamp;

	@CreateUser
	@Column(name="create_user")
	private String createUser;

	@ModificationUser
	@Column(name="modification_user")
	private String modificationUser;

	public VisitId getId() {
		if (id == null) {
			return null;
		}
		VisitId result = new VisitId(id.getFromZipArea(), id.getToCity());
		return result;
	}

	public void setId(VisitId id) {
		if (id == null) {
			return;
		}
		this.id = new VisitId(id.getFromZipArea(), id.getToCity());
	}

	public Long getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(Long numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Calendar getModificationTimestamp() {
		return modificationTimestamp;
	}

	public void setModificationTimestamp(Calendar modificationTimestamp) {
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

	@Internal @ManyToOne
	@JoinColumn(name="from_zip_area", insertable=false, updatable=false)
	private Zip fromZipArea;
	
	@Internal @ManyToOne
	@JoinColumn(name="to_city", insertable=false, updatable=false)
	private City toCity;

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
}
