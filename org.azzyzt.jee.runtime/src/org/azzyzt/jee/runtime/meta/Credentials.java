package org.azzyzt.jee.runtime.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Credentials are used along with the <code>AzzyztGeneratorOption</code> 
 * <code>AddCredentialBasedAuthorization</code> and the annotation
 * <code>RequiresCredentials</code>. Credentials guard service
 * invocations. A principal invoking a service has zero or any number of 
 * credentials associated with the particular service. 
 * <p>
 * Credentials have names and may have zero or any number of properties.
 * Each property has a name and a value. Property names and values are strings,
 * the default value for a property is the string "<code>true</code>".
 * <p>
 * Thus properties without explicitly given value are meant to be understood as
 * flags, modifying the semantics of their owning credential. 
 * <p>
 * Credentials have a string representation. The generic site adapter coming
 * with Azzyzt JEE Tools takes the value of an HTTP header (by default 
 * "<code>x-authorize-roles</code>", but that can be set in the server per 
 * application or server-wide via JNDI string resources), and if the header
 * is set, interprets it as string representation of the invoking principal's
 * credentials.
 * <p>
 * Properties of credentials are specified after the name of the credential in parenthesis. An empty
 * property list results in empty parenthesis. Empty parenthesis can be omitted. 
 * <p>
 * If more than one credential is specified, credentials must be separated with semicolons.
 * White space is not significant. 
 * <p>
 * When using <code>AddCredentialBasedAuthorization</code>, a <code>RequiresCredentials</code>
 * annotation is generated on the <code>ModifyMultiBean</code> and the store and delete methods of the 
 * entity-specific service beans. The string representation of the generated <code>RequiresCredentials</code>
 * annotation is "<code>modify()</code>", meaning a credential "<code>modify</code>" without properties.
 * 
 * The <code>RequiresCredentials</code> annotation can be used in user-written beans. It can be set 
 * on the whole service bean and/or on methods. For any particular invocation the required 
 * credentials are cumulative, with credentials on Methods overriding those on service bean classes. 
 * 
 * @see org.azzyzt.jee.runtime.annotation.RequiresCredentials
 * @see org.azzyzt.jee.runtime.meta.AzzyztGeneratorOption
 */
public class Credentials {

	private Map<String, Credential> creds = new HashMap<String, Credential>();
	
	public static Credentials fromString(String in) {
		Credentials result = new Credentials();
		if (in == null || in.isEmpty()) {
			return result;
		}
		String[] credStatements = in.split(";");
		for (String credStatement : credStatements) {
			// credential name
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
			// credential properties in parens
			String propsPart = credStatementParts[1];
			String[] props = propsPart.split(",");
			for (String prop : props) {
				// isolate name/value pairs
				prop = prop.trim();
				if (prop.isEmpty()) {
					continue;
				}
				String[] p = prop.split("=");
				String pName = p[0].trim();
				if (pName.isEmpty()) {
					continue;
				}
				String pValue = Credential.PROPVAL_TRUE; // default if no value is given
				if (p.length > 1) {
					String p1 = p[1].trim();
					if (!p1.isEmpty()) {
						// has a value
						pValue = p1;
					}
				}
				c.addProperty(pName, pValue);
			}
		}
		return result;
	}

	/**
	 * @param required
	 * @return <code>true</code> if this set of credentials
	 * contains all required credentials, and if a required
	 * credential has properties, all required properties are present and
	 * have the required property values. Additional credentials and 
	 * additional property values are accepted.  
	 */
	public boolean satisfy(Credentials required) {
		if (required == null || required.isEmpty()) {
			// null requirement is satisfied by any mix of credentials
			return true;
		}
		for (String credentialName : required.credentialNameSet()) {
			if (!hasCredential(credentialName)) {
				return false;
			}
			if (!getCredential(credentialName).satisfies(required.getCredential(credentialName))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Merges this credentials with other credentials. As a result, this credentials are equal to
	 * (if originally empty) or a superset of the specified <code>other</code> credentials.
	 * <p>
	 * This credentials' properties are updated with <code>other</code> credentials' properties.
	 * In case of equal property names, <code>other</code>'s values override this' values.
	 * 
	 * @param other
	 */
	public void mergeFrom(Credentials other) {
		if (other == null || other.isEmpty()) {
			return;
		}
		for (String credentialName : other.credentialNameSet()) {
			Credential c = other.getCredential(credentialName);
			if (hasCredential(credentialName)) {
				getCredential(credentialName).mergeFrom(c);
			} else {
				add(credentialName, c);
			}
		}
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
