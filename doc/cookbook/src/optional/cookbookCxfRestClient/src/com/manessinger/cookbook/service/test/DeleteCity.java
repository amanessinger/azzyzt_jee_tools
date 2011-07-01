package com.manessinger.cookbook.service.test;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import com.manessinger.cookbook.service.CityFullCxfRestInterface;

public class DeleteCity {

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
		WebClient.client(citySvc).accept(MediaType.MEDIA_TYPE_WILDCARD);
		
		String result = citySvc.delete(id);
		System.out.println("City with ID "+id+" was deleted ("+result+")");
	}

	private static void usageExit() {
		System.err.println("usage: DeleteCity <base-url> <city-id>");
		System.exit(1);
	}

}
