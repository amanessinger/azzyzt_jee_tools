package org.azzyzt.jee.runtime.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="list")
public class StringListWrapper {
	
	private List<String> item;

	public StringListWrapper() { }
	
	public StringListWrapper(List<String> item) {
		super();
		this.item = item;
	}

	public List<String> getItem() {
		return item;
	}

	public void setItem(List<String> item) {
		this.item = item;
	}

}
