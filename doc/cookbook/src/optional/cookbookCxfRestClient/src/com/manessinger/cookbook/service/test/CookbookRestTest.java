package com.manessinger.cookbook.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXB;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.manessinger.cookbook.dto.CityDto;
import com.manessinger.cookbook.dto.CountryDto;
import com.manessinger.cookbook.dto.Dto;
import com.manessinger.cookbook.service.CityFullCxfRestInterface;
import com.manessinger.cookbook.service.CountryFullCxfRestInterface;
import com.manessinger.cookbook.service.ModifyMultiCxfRestInterface;

/**
 * A test class that executes all tests from the cookbook tutorial. Some tests
 * are slightly altered, in order to make them robust against different IDs and 
 * different execution order. We assume a freshly set up cookbook database.
 */
public class CookbookRestTest {
	
	private static final String BASE_URI = "http://localhost:8080/cookbookServlets/REST";
	
	private static CountryFullCxfRestInterface countrySvc;
	private static CityFullCxfRestInterface citySvc;
	private static ModifyMultiCxfRestInterface multiSvc;
	
	/**
	 * BEFORE CLASS: setup proxies, set their media types to APPLICATION_XML. 
	 * There seems to be an error in Apache CXF, the REST client seemingly ignores 
	 * \@Consumes annotations and always sends text/plain 
	 */
	@BeforeClass
	public static void setupProxies() {
		countrySvc = JAXRSClientFactory.create(BASE_URI, CountryFullCxfRestInterface.class);
		WebClient.client(countrySvc).type(MediaType.APPLICATION_XML);
		
		citySvc = JAXRSClientFactory.create(BASE_URI, CityFullCxfRestInterface.class);
		WebClient.client(citySvc).type(MediaType.APPLICATION_XML);
		
		multiSvc = JAXRSClientFactory.create(BASE_URI, ModifyMultiCxfRestInterface.class);
		WebClient.client(multiSvc).type(MediaType.APPLICATION_XML);
	}
	
	/**
	 * BEFORE TEST: We dump to System.out, print a line between tests
	 */
	@Before
	public void printDelimiter() {
		System.err.println("########################################################\n");
	}
	
	/**
	 * TEST: List of all countries
	 */
	@Test
	public void testAllCountries() {
		List<Dto> countries = countrySvc.all();
		
		assertNotNull(countries);
		assertTrue(countries.size() >= 3);
		for (Dto d : countries) {
			assertTrue(d instanceof CountryDto);
			CountryDto c = (CountryDto)d;
			dump(c);
		}
	}
	
	/**
	 * TEST: City with the ID 1
	 */
	@Test
	public void testCityById() {
		CityDto city1 = citySvc.byId(1L);
		
		assertNotNull(city1);
		assertEquals(new Long(1), city1.getId());
		assertEquals("Graz", city1.getName());
		dump(city1);
	}
	
	/**
	 * TEST: Sorted list of cities, grouped by ascending country.ID, names within groups descending
	 */
	@Test
	public void testSortedListOfCities() {
		List<Dto> cities = citySvc.list(from("META-INF/xml/sorted_list_of_cities.xml"));
		
		assertNotNull(cities);
		assertTrue(cities.size() >= 12);
		CityDto last = null;
		for (Dto d : cities) {
			assertTrue(d instanceof CityDto);
			CityDto c = (CityDto)d;
			if (last != null) {
				if (c.getCountryId().equals(last.getCountryId())) {
					// we expect names within country groups to be descending, could be equal
					assertTrue(c.getName().compareTo(last.getName()) <= 0);
				} else {
					assertTrue(c.getCountryId() > last.getCountryId());
				}
			}
			last = c;
			dump(c);
		}
	}

	/**
	 * TEST: Query with three conditions:
	 *  - Country name is "Italy"
	 *  - City name does not begin with "r" (case-insensitive)
	 *  - City ID is not 8 
	 *  - ascending by city name
	 */
	@Test
	public void testQueryWithThreeConditions() {
		List<Dto> cities = citySvc.list(from("META-INF/xml/query_with_three_conditions.xml"));
		
		assertNotNull(cities);
		assertTrue(cities.size() >= 2);
		CountryDto co = null;
		CityDto last = null;
		for (Dto d : cities) {
			assertTrue(d instanceof CityDto);
			CityDto c = (CityDto)d;
			
			// is in Italy
			if (co == null) {
				co = countrySvc.byId(c.getCountryId());
				assertEquals("Italy", co.getName());
			} else {
				assertEquals(co.getId(), c.getCountryId());
			}
			
			// does not begin with "r" (case-insensitive)
			char startChar = c.getName().charAt(0);
			assertTrue(startChar != 'r' && startChar != 'R');
			
			// ID is not 8
			assertTrue(c.getId() != 8);
			
			// name ascending
			if (last != null) {
				assertTrue(c.getName().compareTo(last.getName()) >= 0);
			}
			last = c;

			dump(c);
		}
	}

	/**
	 * TEST: Nested expressions
	 *  - either in "Italy"
	 *  - name starts with "l" (case-insensitive), but is not "Linz"
	 */
	@Test
	public void testNestedExpressions() {
		List<Dto> cities = citySvc.list(from("META-INF/xml/nested_expressions.xml"));
		
		assertNotNull(cities);
		assertTrue(cities.size() >= 2);
		CityDto last = null;
		for (Dto d : cities) {
			assertTrue(d instanceof CityDto);
			CityDto c = (CityDto)d;
			
			CountryDto co = countrySvc.byId(c.getCountryId());
			if (!co.getName().equals("Italy")) {
				char startChar = c.getName().charAt(0);
				assertTrue(startChar == 'l' || startChar == 'L');
				assertTrue(c.getId() != 2);
				assertTrue(!c.getName().equals("Linz"));
			}
			
			// name ascending
			if (last != null) {
				assertTrue(c.getName().compareTo(last.getName()) >= 0);
			}
			last = c;

			dump(c);
		}
	}
	
	/**
	 * Store a new city under a wrong name, update its name, finally delete it.
	 * This is different from the tutorial, we use Austria in order to not 
	 * interfere with the list tests so far. We also do it only once, because
	 * we don't test JSON.
	 */
	@Test
	public void testStoreUpdateDeleteCity() {
		List<Dto> countries = countrySvc.list(from("META-INF/xml/get_austria.xml"));
		assertNotNull(countries);
		assertEquals(1, countries.size());
		CountryDto austria = (CountryDto)countries.get(0);
		assertEquals("Austria", austria.getName());
		
		CityDto c = new CityDto();
		c.setCountryId(austria.getId());
		
	}

	/**
	 * Prints the name of the calling method, followd by an XML dump of object given as parameter
	 * @param o
	 */
	private static void dump(Object o) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		System.err.println(trace[2].getMethodName()+"():\n");
		JAXB.marshal(o, System.err);
		System.err.println("\n");
	}
	
	/**
	 * Read content of a file and returns it as a string
	 * @param relativeToRootFileName
	 * @return
	 */
	private String from(String relativeToRootFileName) {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
			    getClass().getClassLoader().getResourceAsStream(
			    		relativeToRootFileName)));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				result.append(line);
				result.append('\n');
			}
		} catch (IOException e) {
			throw new Error(e);
		}
		return result.toString().trim();
	}
	
}
