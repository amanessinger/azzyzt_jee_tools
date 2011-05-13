package com.manessinger.cookbook.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="visitId")
@Embeddable
public class VisitId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="from_zip_area", nullable = false)
	private Long fromZipArea;

	@Column(name="to_city", nullable = false)
	private Long toCity;

	public VisitId() {
		super();
	}
	
	public VisitId(Long fromZipArea, Long toCity) {
		super();
		this.fromZipArea = fromZipArea;
		this.toCity = toCity;
	}

	public Long getFromZipArea() {
		return fromZipArea;
	}

	public void setFromZipArea(Long fromZipArea) {
		this.fromZipArea = fromZipArea;
	}

	public Long getToCity() {
		return toCity;
	}

	public void setToCity(Long toCity) {
		this.toCity = toCity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromZipArea == null) ? 0 : fromZipArea.hashCode());
		result = prime * result + ((toCity == null) ? 0 : toCity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisitId other = (VisitId) obj;
		if (fromZipArea == null) {
			if (other.fromZipArea != null)
				return false;
		} else if (!fromZipArea.equals(other.fromZipArea))
			return false;
		if (toCity == null) {
			if (other.toCity != null)
				return false;
		} else if (!toCity.equals(other.toCity))
			return false;
		return true;
	}
	
}
