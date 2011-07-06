package com.manessinger.cookbook.service.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import com.manessinger.cookbook.dto.CityDto;
import com.manessinger.cookbook.service.CityFullCxfRestInterface;

public class CityById {

	public static void main(String[] args) {
		
		if (args.length != 2) {
			usageExit();
		}

		URL base = null;
		try {
			base = new URL(args[0]);
		} catch (MalformedURLException e) {
			usageExit();
		}
		
		Long id = null;
		try {
			id = Long.parseLong(args[1]);
		} catch (NumberFormatException e) {
			usageExit();
		}
		
		CityFullCxfRestInterface citySvc = JAXRSClientFactory.create(base.toExternalForm(), CityFullCxfRestInterface.class);
		
		CityDto city = citySvc.byId(id);
		System.out.println("City with ID "+id+" is "+city.getName());
	}

	private static void usageExit() {
		System.err.println("usage: CityById <base-url> <city-id>");
		System.exit(1);
	}

}
