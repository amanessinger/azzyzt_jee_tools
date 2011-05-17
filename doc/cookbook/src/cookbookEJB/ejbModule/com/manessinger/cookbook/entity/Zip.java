package com.manessinger.cookbook.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@EntityListeners({StandardEntityListeners.class})
public class Zip extends EntityBase<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ZIP_ID_GENERATOR", sequenceName = "ZIP_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZIP_ID_GENERATOR")
	private Long id;

	private String code;
	
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

	// bi-directional many-to-one association to Country
	@ManyToOne
	private Country country;

    @OneToMany(mappedBy="fromZipArea")
	private List<Visit> visits;
    
	public Zip() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
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

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}
}