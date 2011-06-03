package org.azzyzt.jee.tools.project;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AzzyztProperties {
	
	private String version;
	
	public static AzzyztProperties fromXml(String xml) 
	throws Exception 
	{
    	try {
    		AzzyztProperties result = new AzzyztProperties();
    		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));

    		Document doc = db.parse(is);
    		
    		XPathFactory xPathFactory = XPathFactory.newInstance();
    		XPath xpath = xPathFactory.newXPath();
    		
    		String expr = "/azzyzted_project/azzyzt_version";
    		NodeList nodes = (NodeList) xpath.evaluate(
    				expr, doc,
    				XPathConstants.NODESET);
    		if (nodes.getLength() == 1) {
    			String azzyztVersion = nodes.item(0).getNodeValue();
    			result.setVersion(azzyztVersion);
    			return result;
    		}
    	} catch (Exception e) { }
    	throw new Exception("Can't parse AzzyztProperties from string\n\""+xml+"\"");
	}

	public AzzyztProperties() { }

	public AzzyztProperties(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String toXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<azzyzted_project>\n");
		sb.append("  <azzyzt_version>");
		sb.append(version);
		sb.append("</azzyzt_version>\n");
		sb.append("</azzyzted_project>\n");
		return sb.toString();
	}
}
