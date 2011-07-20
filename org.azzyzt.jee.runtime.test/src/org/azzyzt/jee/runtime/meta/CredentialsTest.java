package org.azzyzt.jee.runtime.meta;

import static org.junit.Assert.*;

import org.junit.Test;

public class CredentialsTest {
	
	private static final String CRED_ONE = "one";

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

	private void assertEmptyCredentials(Credentials cred) {
		assertNotNull(cred);
		assertTrue(cred.isEmpty());
	}
	
	private void assertHasOnlyCredOne(Credentials cred) {
		assertNotNull(cred);
		assertFalse(cred.isEmpty());
		assertEquals(1, cred.numberOfCredentials());
		assertTrue(cred.credentialNameSet().contains(CRED_ONE));
		Credential c = cred.getCredential(CRED_ONE);
		assertNotNull(c);
		assertEquals(CRED_ONE, c.getName());
	}
	
}
