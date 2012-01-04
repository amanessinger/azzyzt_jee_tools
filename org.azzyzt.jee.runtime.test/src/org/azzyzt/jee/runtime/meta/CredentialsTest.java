package org.azzyzt.jee.runtime.meta;

import static org.junit.Assert.*;

import org.junit.Test;

public class CredentialsTest {
	
	private static final String CRED_ONE = "one";
	private static final String CRED_TWO = "two";

	@Test
	public void testNull() {
		Credentials cred = Credentials.fromString(null);
		assertEmptyCredentials(cred);
	}

	@Test
	public void testEmpty() {
		Credentials cred = Credentials.fromString("");
		assertEmptyCredentials(cred);
	}
		
	@Test
	public void testSepOnly() {
		Credentials cred = Credentials.fromString(";");
		assertEmptyCredentials(cred);
	}

	@Test
	public void testOneSimple() {
		Credentials cred = Credentials.fromString(CRED_ONE);
		assertHasOnlyCredOne(cred);
	}

	@Test
	public void testOneSimpleParen() {
		Credentials cred = Credentials.fromString(CRED_ONE+"()");
		assertHasOnlyCredOne(cred);
	}

	@Test
	public void testTwoSimple() {
		Credentials cred = Credentials.fromString(CRED_ONE+";"+CRED_TWO);
		assertHasOneAndTwo(cred);
	}

	@Test
	public void testTwoSimpleIrregular() {
		Credentials cred = Credentials.fromString("  "+CRED_ONE+" ; "+CRED_TWO+"() ; ");
		assertHasOneAndTwo(cred);
	}

	@Test
	public void testTwoWithProperties() {
		Credentials cred = Credentials.fromString(CRED_ONE+"(A=x,B=y);"+CRED_TWO+"(C=z)");
		assertHasOneAndTwo(cred);
		assertHasProp(cred.getCredential(CRED_ONE), "A", "x");
		assertHasProp(cred.getCredential(CRED_ONE), "B", "y");
		assertHasProp(cred.getCredential(CRED_TWO), "C", "z");
	}

	@Test
	public void testOneWithPropertiesAndDefault() {
		Credentials cred = Credentials.fromString(CRED_ONE+"(A,B)");
		assertHasOnlyCredOne(cred);
		Credential c = cred.getCredential(CRED_ONE);
		// general way
		assertHasProp(c, "A", Credential.PROPVAL_TRUE);
		assertHasProp(c, "B", Credential.PROPVAL_TRUE);
		// check trueness
		assertTrue(c.isPropertyTrue("A"));
		assertTrue(c.isPropertyTrue("B"));
	}

	@Test
	public void testTwoWithPropertiesIrregular() {
		Credentials cred = Credentials.fromString(CRED_ONE+"( A = x , B = y );"+CRED_TWO+"(C)");
		assertHasOneAndTwo(cred);
		assertHasProp(cred.getCredential(CRED_ONE), "A", "x");
		assertHasProp(cred.getCredential(CRED_ONE), "B", "y");
		assertHasProp(cred.getCredential(CRED_TWO), "C", Credential.PROPVAL_TRUE);
	}

	private void assertEmptyCredentials(Credentials cred) {
		assertNotNull(cred);
		assertTrue(cred.isEmpty());
	}
	
	private void assertHasOnlyCredOne(Credentials cred) {
		assertHasNCreds(cred, 1);
		assertHasCred(cred, CRED_ONE);
	}

	private void assertHasNCreds(Credentials cred, int n) {
		assertNotNull(cred);
		assertFalse(cred.isEmpty());
		assertEquals(n, cred.numberOfCredentials());
	}

	private void assertHasCred(Credentials cred, String name) {
		assertTrue(cred.credentialNameSet().contains(name));
		Credential c = cred.getCredential(name);
		assertNotNull(c);
		assertEquals(name, c.getName());
	}
	
	private void assertHasOneAndTwo(Credentials cred) {
		assertHasNCreds(cred, 2);
		assertHasCred(cred, CRED_ONE);
		assertHasCred(cred, CRED_TWO);
	}
	
	private void assertHasProp(Credential c, String propName, String propVal) {
		assertTrue(c.hasProperty(propName));
		assertEquals(propVal, c.getPropertyValue(propName));
	}
}

