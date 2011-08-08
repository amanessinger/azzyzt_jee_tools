package com.manessinger.cookbook.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXB;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.manessinger.cookbook.dto.CityDto;
import com.manessinger.cookbook.dto.CountryDto;
import com.manessinger.cookbook.dto.Dto;
import com.manessinger.cookbook.dto.LanguageDto;
import com.manessinger.cookbook.dto.StoreDelete;
import com.manessinger.cookbook.dto.VisitDto;
import com.manessinger.cookbook.entity.VisitId;
import com.manessinger.cookbook.service.CityFullCxfRestInterface;
import com.manessinger.cookbook.service.CountryFullCxfRestInterface;
import com.manessinger.cookbook.service.LanguageFullCxfRestInterface;
import com.manessinger.cookbook.service.ModifyMultiCxfRestInterface;
import com.manessinger.cookbook.service.VisitFullCxfRestInterface;
import com.manessinger.cookbook.service.ZipFullCxfRestInterface;

/**
 * A test class that executes all tests from the cookbook tutorial. Some tests
 * are slightly altered, in order to make them robust against different IDs and 
 * different execution order. We assume a freshly set up cookbook database.
 */
public class CookbookRestTest {
	
	private static final String BASE_URI = "http://localhost:8081/cookbookServlets/REST";
	
	private static final String FRANCE = "France";
	private static final String MARSEILLES = "Marseilles";
	private static final String PARIS = "Paris";
	private static final String RENNES = "Rennes";
	
	private static final String EGYPT = "Egypt";
	private static final String ASSUAN = "Assuan";
	private static final String CAIRO = "Cairo";
	
	private static CityFullCxfRestInterface citySvc;
	private static CountryFullCxfRestInterface countrySvc;
	private static LanguageFullCxfRestInterface languageSvc;
	private static ModifyMultiCxfRestInterface multiSvc;
	private static VisitFullCxfRestInterface visitSvc;
	private static ZipFullCxfRestInterface zipSvc;

	private static Client cityClient;
	private static Client countryClient;
	private static Client languageClient;
	private static Client multiClient;
	private static Client visitClient;
	private static Client zipClient;
	
	private static CityFullCxfRestInterface cityProtectedSvc;
	private static Client cityProtectedClient;
	
	/**
	 * BEFORE CLASS: setup proxies, set their media types to APPLICATION_XML. 
	 * There seems to be an error in Apache CXF, the REST client seemingly ignores 
	 * \@Consumes annotations and always sends text/plain 
	 * 
	 * The "Accept: *" header seems to be necessary to make a returned string (delete) 
	 * be delivered as XML. Omitting it makes delete() fail with the generic 
	 * 
	 * javax.ws.rs.WebApplicationException 
	 *   at com.sun.jersey.server.impl.uri.rules.TerminatingRule.accept(TerminatingRule.java:66)
	 * 
	 * No idea whose fault this is. I suppose how I treat delete() is wrong (though it works for 
	 * soapUI and for Flex clients), otoh I don't see why CXF automatically produces an 
	 * "Accept: text/plain" for a return type of String. It could simply use the type from the 
	 * interface, and that's "application/xml".
	 */
	@BeforeClass
	public static void setupProxies() {

		citySvc = JAXRSClientFactory.create(BASE_URI, CityFullCxfRestInterface.class);
		cityClient = WebClient.client(citySvc);
		cityClient.type(MediaType.APPLICATION_XML);
		cityClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		cityClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		cityClient.header("x-authenticate-userid", "junit");
		
		countrySvc = JAXRSClientFactory.create(BASE_URI, CountryFullCxfRestInterface.class);
		countryClient = WebClient.client(countrySvc);
		countryClient.type(MediaType.APPLICATION_XML);
		countryClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		countryClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		countryClient.header("x-authenticate-userid", "junit");
		
		languageSvc = JAXRSClientFactory.create(BASE_URI, LanguageFullCxfRestInterface.class);
		languageClient = WebClient.client(languageSvc);
		languageClient.type(MediaType.APPLICATION_XML);
		languageClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		languageClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		languageClient.header("x-authenticate-userid", "junit");
		
		multiSvc = JAXRSClientFactory.create(BASE_URI, ModifyMultiCxfRestInterface.class);
		multiClient = WebClient.client(multiSvc);
		multiClient.type(MediaType.APPLICATION_XML);
		multiClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		multiClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		multiClient.header("x-authenticate-userid", "junit");
		
		visitSvc = JAXRSClientFactory.create(BASE_URI, VisitFullCxfRestInterface.class);
		visitClient = WebClient.client(visitSvc);
		visitClient.type(MediaType.APPLICATION_XML);
		visitClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		visitClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		visitClient.header("x-authenticate-userid", "junit");
		
		zipSvc = JAXRSClientFactory.create(BASE_URI, ZipFullCxfRestInterface.class);
		zipClient = WebClient.client(zipSvc);
		zipClient.type(MediaType.APPLICATION_XML);
		zipClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		zipClient.header("x-authorize-roles", "azzyzt(200-on-error=false);modify()");
		zipClient.header("x-authenticate-userid", "junit");
		
		cityProtectedSvc = JAXRSClientFactory.create(BASE_URI, CityFullCxfRestInterface.class);
		cityProtectedClient = WebClient.client(cityProtectedSvc);
		cityProtectedClient.type(MediaType.APPLICATION_XML);
		cityProtectedClient.accept(MediaType.MEDIA_TYPE_WILDCARD);
		cityProtectedClient.header("x-authorize-roles", "azzyzt(200-on-error=false);");
		cityProtectedClient.header("x-authenticate-userid", "junit");
		
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
	 * TEST: Sorted list of cities, grouped by ascending country.ID, 
	 * names within groups descending
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
	 * TEST: Store, update and delete a city
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
		c.setName("Villak");
		dump(c);
		
		c = citySvc.store(c);
		assertNotNull(c);
		assertNotNull(c.getId());
		assertTrue(c.getId() > 12);
		assertNotNull(c.getCreateTimestamp());
		assertNotNull(c.getModificationTimestamp());
		assertNotNull(c.getCreateUser());
		assertNotNull(c.getModificationUser());
		assertEquals(c.getCreateTimestamp(), c.getModificationTimestamp());
		assertEquals(c.getCreateUser(), c.getModificationUser());
		dump(c);

		Long id = c.getId();
		Calendar createTst = c.getCreateTimestamp();
		
		c.setName("Villach");
		c = citySvc.store(c);
		assertNotNull(c);
		assertNotNull(c.getId());
		assertEquals(id, c.getId());
		assertNotNull(c.getModificationTimestamp());
		assertTrue(c.getModificationTimestamp().compareTo(createTst) == 1);
		dump(c);
		
		String result = citySvc.delete(id);
		assertEquals("<result>OK</result>", result);
	}
	
	/**
	 * TEST: store France and three cities in one call, delete them in one call
	 */
	@Test
	public void testStoreDeleteFranceAndCities() {
		List<Dto> dtos = createCountryAndCityDtos(FRANCE, MARSEILLES, PARIS, RENNES);
		dtos = multiSvc.storeMulti(dtos);
		checkCountryAndCityDtos(dtos, FRANCE, MARSEILLES, PARIS, RENNES);
		
		dump(dtos);

		String result = multiSvc.deleteMulti(dtos);
		assertEquals("<result>OK</result>", result);
	}

	/**
	 * TEST: store France, replace it with Egypt, make a visit to Luxor 
	 * and clean up
	 */
	@Test
	public void testStoreFranceReplaceWithEgyptMakeVisit() {
		List<Dto> france = createCountryAndCityDtos(FRANCE, MARSEILLES, PARIS, RENNES);
		france = multiSvc.storeMulti(france);
		checkCountryAndCityDtos(france, FRANCE, MARSEILLES, PARIS, RENNES);
		
		dump(france);

		Long idFrance = ((CountryDto)france.get(0)).getId();

		List<Dto> egypt = createCountryAndCityDtos(EGYPT, ASSUAN, CAIRO);

		StoreDelete sd = new StoreDelete(france, egypt);
		egypt = multiSvc.storeDeleteMulti(sd);
		// check (partially) that France is gone
		boolean lookupFailed = true;
		try {
			countrySvc.byId(idFrance);
			lookupFailed = false;
		} catch (WebApplicationException e) { }
		assertTrue(lookupFailed);
		// now check Egypt
		checkCountryAndCityDtos(egypt, EGYPT, ASSUAN, CAIRO);
		
		dump(egypt);
		
		// create the visit
		Long idEgypt = ((CountryDto)egypt.get(0)).getId();
		
		CityDto luxor = createCity(idEgypt, "Luxor", -1L);
		
		LanguageDto enUK = new LanguageDto();
		enUK.setId("en_UK");
		enUK.setLanguageName("English (UK)");
		
		VisitDto visit = new VisitDto();
		visit.setId(new VisitId(1L, luxor.getId(), enUK.getId()));
		visit.setTotalNumberOfVisitors(5L);
		
		List<Dto> dtos = new ArrayList<Dto>();
		dtos.add(luxor);
		dtos.add(enUK);
		dtos.add(visit);
		dtos = multiSvc.storeMulti(dtos);
		
		assertNotNull(dtos);
		assertEquals(3, dtos.size());
		
		Dto dto = dtos.get(0);
		assertNotNull(dto);
		assertTrue(dto instanceof CityDto);
		CityDto luxorStored = (CityDto)dto;
		assertTrue(luxorStored.getId() > 0);
		
		dto = dtos.get(2);
		assertNotNull(dto);
		assertTrue(dto instanceof VisitDto);
		VisitDto visitStored = (VisitDto)dto;
		assertEquals(luxorStored.getId(), visitStored.getId().getToCity());
		
		dump(dtos);
		
		String result = multiSvc.deleteMulti(dtos);
		assertEquals("<result>OK</result>", result);
		
		result = multiSvc.deleteMulti(egypt);
		assertEquals("<result>OK</result>", result);
	}

	/**
	 * TEST: Store a city with the protected client. It does not send a 
	 * "modify" credential, thus the server should throw an AccessDenied Exception.
	 */
	@Test(expected=ServerWebApplicationException.class)
	public void testTryStoreCity() {
		List<Dto> countries = countrySvc.list(from("META-INF/xml/get_austria.xml"));
		assertNotNull(countries);
		assertEquals(1, countries.size());
		CountryDto austria = (CountryDto)countries.get(0);
		assertEquals("Austria", austria.getName());
		
		CityDto c = new CityDto();
		c.setCountryId(austria.getId());
		c.setName("Villach");
		dump(c);
		
		c = cityProtectedSvc.store(c);
		fail("This should be unreachable");
	}
	
	/**
	 * Helper method that creates DTOs for a country and its cities
	 * 
	 * @param name of country
	 * @param names of cities
	 * @return
	 */
	private List<Dto> createCountryAndCityDtos(String country, String...cities) {
		List<Dto> result = new ArrayList<Dto>();

		Long idGen = -1L;
		CountryDto co = new CountryDto();
		co.setId(idGen--);
		co.setName(country);
		result.add(co);

		if (cities == null || cities.length == 0) return result;
		
		for (String city : cities) {
			CityDto c = createCity(co.getId(), city, idGen--);
			result.add(c);
		}
		return result;
	}

	/**
	 * Helper method to create a city
	 * 
	 * @param cityName
	 * @param countryId
	 * @return
	 */
	private CityDto createCity(Long countryId, String cityName, Long cityId) {
		CityDto c = new CityDto();
		c.setCountryId(countryId);
		c.setId(cityId);
		c.setName(cityName);
		return c;
	}

	/**
	 * Helper method that checks a country and its cities
	 * 
	 * @param dtos returned from storeMulti() or storeDeleteMulti()
	 * @param name of country
	 * @param names of cities
	 */
	private void checkCountryAndCityDtos(List<Dto> dtos, String country, String...cities) {
		// guard against caller
		assertTrue(cities != null);

		Dto dto;

		// check list
		assertNotNull(dtos);
		assertEquals(1 + cities.length, dtos.size());
		
		dto = dtos.get(0);
		assertNotNull(dto);
		assertTrue(dto instanceof CountryDto);
		
		CountryDto co = (CountryDto)dto;
		Long countryId = co.getId();
		assertNotNull(countryId);
		assertTrue(countryId > 0);
		assertEquals(country, co.getName());
		
		if (cities.length == 0) return;
		
		for (int i = 0; i < cities.length; i++) {
			dto = dtos.get(i + 1);
			assertNotNull(dto);
			assertTrue(dto instanceof CityDto);
			
			CityDto c = (CityDto)dto;
			Long cityId = c.getId();
			assertNotNull(cityId);
			assertTrue(cityId > 0);
			assertEquals(cities[i], c.getName());
			assertNotNull(c.getCountryId());
			assertEquals(countryId, c.getCountryId());
		}
	}

	/**
	 * Prints the name of the calling method, followed by an XML dump 
	 * of object given as parameter
	 * @param o
	 */
	private static void dump(Object o) {
		dump(o, 3);
	}
	
	private static void dump(Object o, int depth) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		System.err.println(trace[depth].getMethodName()+"():\n");
		JAXB.marshal(o, System.err);
		System.err.println("\n");
	}
	
	private static void dump(List<?> l) {
		for (Object o : l) {
			dump(o, 3);
		}
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
