package org.azzyzt.jee.tools.mwe.builder;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.azzyzt.jee.tools.mwe.exception.ToolError;

public class EntityEnumerator implements TargetEnumerator {

	private static Logger logger = Logger.getLogger(EntityEnumerator.class.getPackage().getName());
	
    public static final String PERSISTENCE_UNIT_WILDCARD = "*";

    private String persistenceUnitName = PERSISTENCE_UNIT_WILDCARD;
    
    private List<String> fullyQualifiedTargetNames = new ArrayList<String>();
    private Set<String> packageNames = new HashSet<String>();

    public EntityEnumerator (String pUnitName) {
        this.persistenceUnitName = pUnitName;
        
        String resourceName = "META-INF/persistence.xml";
        List<URL> resourcesFound = locateResources(resourceName);
        for (URL u : resourcesFound) {
        	try {
        		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        		DocumentBuilder db = dbf.newDocumentBuilder();
        		InputSource is = new InputSource();
        		is.setCharacterStream(new FileReader(u.getFile()));
        		
        		Document doc = db.parse(is);
        		
        		XPathFactory xPathFactory = XPathFactory.newInstance();
        		XPath xpath = xPathFactory.newXPath();
        		
        		String expr;
        		if (!persistenceUnitName.equals(PERSISTENCE_UNIT_WILDCARD)) {
        			expr = "/persistence/persistence-unit[@name='"
        				+ persistenceUnitName + "']/class/text()";
        		} else {
        			expr = "/persistence/persistence-unit/class/text()";
        		}
        		NodeList nodes = (NodeList) xpath.evaluate(
        				expr, doc,
        				XPathConstants.NODESET);
        		logger.info("Found " + nodes.getLength() + " classes");
        		
        		for (int i = 0; i < nodes.getLength(); i++) {
        			fullyQualifiedTargetNames.add(nodes.item(i).getNodeValue());
        		}
        		for (String fqtn : fullyQualifiedTargetNames) {
        			String pkg = fqtn.substring(0, fqtn.lastIndexOf("."));
        			packageNames.add(pkg);
        		}
        	} catch (Exception e) {
        		e.printStackTrace();
        		throw new ToolError(e);
        	}
        }
    }

    /* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.TargetEnumerator#getFullyQualifiedTargetNames()
	 */
    public List<String> getFullyQualifiedTargetNames() {
        return fullyQualifiedTargetNames;
    }
    
    /* (non-Javadoc)
	 * @see org.azzyzt.jee.tools.mwe.builder.TargetEnumerator#getTargetPackageNames()
	 */
    public Set<String> getTargetPackageNames() {
    	return packageNames;
    }

    private List<URL> locateResources(String resourceName) {
        List<URL> resourcesFound = new ArrayList<URL>();
        Enumeration<URL> found;
        try {
            found = Thread.currentThread().getContextClassLoader()
                    .getResources(resourceName);
        } catch (IOException e) {
            throw new ToolError(e);
        }
        if (null == found) {
            throw new ToolError("No resource of name '" + resourceName
                    + "' found in classpath");
        }
        while (found.hasMoreElements()) {
            resourcesFound.add(found.nextElement());
        }
        return resourcesFound;
    }

}
