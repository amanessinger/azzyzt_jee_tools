package com.manessinger.cookbook.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.azzyzt.jee.runtime.annotation.CreateTimestamp;
import org.azzyzt.jee.runtime.annotation.CreateUser;
import org.azzyzt.jee.runtime.annotation.ModificationTimestamp;
import org.azzyzt.jee.runtime.annotation.ModificationUser;
import org.azzyzt.jee.runtime.entity.EntityBase;

@Entity
public class Country extends EntityBase<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "COUNTRY_ID_GENERATOR", sequenceName = "COUNTRY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRY_ID_GENERATOR")
	private Long id;
	
	private String name;
	
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

	// bi-directional many-to-one association to City
	@OneToMany(mappedBy = "country")
	private Set<City> cities;

	public Country() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<City> getCities() {
		return this.cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
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
}