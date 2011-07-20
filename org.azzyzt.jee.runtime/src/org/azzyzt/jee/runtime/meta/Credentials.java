package org.azzyzt.jee.runtime.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Credentials {

	private Map<String, Credential> creds = new HashMap<String, Credential>();
	
	public static Credentials fromString(String in) {
		Credentials result = new Credentials();
		if (in == null || in.isEmpty()) {
			return result;
		}
		String[] credStatements = in.split(";");
		for (String credStatement : credStatements) {
			credStatement = credStatement.trim();
			if (credStatement.isEmpty()) {
				continue;
			}
			String[] credStatementParts = credStatement.split("[()]");
			if (!credStatementParts[0].matches("^\\w+$")) {
				continue;
			}
			String credentialName = credStatementParts[0];
			Credential c = new Credential(credentialName);
			result.add(credentialName, c);
			if (credStatementParts.length == 1) {
				continue;
			}
		}
		return result;
	}

	public boolean hasCredential(String credentialName) {
		return creds.containsKey(credentialName);
	}

	public Credential getCredential(String credentialName) {
		return creds.get(credentialName);
	}

	public boolean isEmpty() {
		return creds.isEmpty();
	}

	public Set<String> credentialNameSet() {
		return creds.keySet();
	}

	public int numberOfCredentials() {
		return creds.size();
	}

	public Credential add(String credentialName, Credential c) {
		return creds.put(credentialName, c);
	}

	
}
