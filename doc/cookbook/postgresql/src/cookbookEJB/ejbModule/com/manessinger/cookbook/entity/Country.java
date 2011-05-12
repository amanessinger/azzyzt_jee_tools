package com.manessinger.cookbook.entity;
 
import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.azzyzt.jee.runtime.entity.EntityBase;
 
@Entity
public class Country extends EntityBase<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
	@SequenceGenerator(name="COUNTRY_ID_GENERATOR", sequenceName="COUNTRY_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COUNTRY_ID_GENERATOR")
    private Long id;
    private String name;
 
    //bi-directional many-to-one association to City
    @OneToMany(mappedBy="country")
    private Set<City> cities;
 
    public Country() { }
 
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
 
    public Set<City> getCities() { return this.cities; }
    public void setCities(Set<City> cities) { this.cities = cities; }    
}