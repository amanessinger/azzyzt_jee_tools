package com.manessinger.cookbook.entity;
 
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.azzyzt.jee.runtime.entity.EntityBase;
 
@Entity
public class Zip extends EntityBase<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
	@SequenceGenerator(name="ZIP_ID_GENERATOR", sequenceName="ZIP_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ZIP_ID_GENERATOR")
    private Long id;
 
    private String code;
    private String name;
 
    //bi-directional many-to-one association to Country
    @ManyToOne
    private Country country;
 
    public Zip() { }
 
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return this.code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
 
    public Country getCountry() { return this.country; }
    public void setCountry(Country country) { this.country = country; }    
}