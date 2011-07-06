package com.manessinger.cookbook.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.azzyzt.jee.runtime.entity.EntityBase;

@Entity
@Table(name="lang_table")
public class Language extends EntityBase<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="lang_code")
	private String id;
	
	@Column(name="lang_name")
	private String languageName;
	
    @OneToMany(mappedBy="languageUsedByGuide")
	private List<Visit> visits;
    
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

}
