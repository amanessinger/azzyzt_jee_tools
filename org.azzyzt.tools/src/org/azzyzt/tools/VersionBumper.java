package org.azzyzt.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class VersionBumper {

	private static final String DOTTED_VERSION_EXPR = "\\d+(\\.\\d+){2}";
	private static final String VERSION_SUFFIX_FQ_EXPR = "\\.\\d{12}";
	private static final String VERSION_SUFFIX_GENERIC = "qualifier";
	private static final String VERSION_SUFFIX_GENERIC_EXPR = "\\."+VERSION_SUFFIX_GENERIC;

	private static final String SITE_TARGET_XPATH = "/site/feature";
	private static final String[] SITE_TARGET_ATTRIBUTES = {
		"url", 
		"version",
	};
	
	private static final String FEATURE_TARGET_XPATH = "/feature";
	private static final String[] FEATURE_TARGET_ATTRIBUTES = {
		"version",
	};
	
	private static final String MANIFEST_LINE_PREFIX = "Bundle-Version: ";
	private static final String MANIFEST_LINE_REGEXP = "^"+MANIFEST_LINE_PREFIX+DOTTED_VERSION_EXPR+"."+VERSION_SUFFIX_GENERIC;
	
	private static final String VIKI_LINE_PREFIX = "#VAR: version=";
	private static final String VIKI_LINE_REGEXP = "^"+VIKI_LINE_PREFIX+DOTTED_VERSION_EXPR;
	
	private static final String[] SITES = {
		"azzyzt_generic/site.xml",
		"azzyzt_magwien/site.xml",
	};
	
	private static final String[] FEATURES = {
		"org.azzyzt.jee.mwe.generic.feature/feature.xml",
		"org.azzyzt.jee.mwe.magwien.feature/feature.xml",
	};
	
	private static final String[] MANIFESTS = {
		"org.azzyzt.jee.runtime.site.generic.fragment/META-INF/MANIFEST.MF",
		"org.azzyzt.jee.runtime.site.magwien.fragment/META-INF/MANIFEST.MF",
		"org.azzyzt.jee.tools.common.plugin/META-INF/MANIFEST.MF",
		"org.azzyzt.jee.tools.mwe.projectgen.plugin/META-INF/MANIFEST.MF",
		"org.azzyzt.jee.tools.project.plugin/META-INF/MANIFEST.MF",
	};
	
	private static final String VIKI_DIR = "doc";
	private static final String VIKI_EXT = ".viki";

	private static final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public static void main(String[] args) {
		
		if (args.length != 1) {
			usageAndExit();
		}
		
		String newVersion = args[0];
		if (!newVersion.matches("^"+DOTTED_VERSION_EXPR+"$")) {
			usageAndExit();
		}

		bumpTo(newVersion);
	}

	private static void bumpTo(String newVersion) {
		String qualifiedVersion = newVersion+"."+VERSION_SUFFIX_GENERIC;
		for (String site : SITES) {
			bumpXMLTo(site, SITE_TARGET_XPATH, SITE_TARGET_ATTRIBUTES, VERSION_SUFFIX_FQ_EXPR, qualifiedVersion);
		}
		for (String feature : FEATURES) {
			bumpXMLTo(feature, FEATURE_TARGET_XPATH, FEATURE_TARGET_ATTRIBUTES, VERSION_SUFFIX_GENERIC_EXPR, qualifiedVersion);
		}
		for (String manifest : MANIFESTS) {
			bumpTextTo(manifest, MANIFEST_LINE_REGEXP, MANIFEST_LINE_PREFIX+qualifiedVersion, 1);
		}
		bumpTextsTo(VIKI_DIR, VIKI_EXT, VIKI_LINE_REGEXP, VIKI_LINE_PREFIX+newVersion, 1);
	}

	private static void bumpXMLTo(String filename, String xpathExpression, String[] targetAttributes, String suffix, String newVersion) {
		try {
			Document doc = openAsDOM(filename);
		    XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(
					xpathExpression, doc, XPathConstants.NODESET
			);
			int nodeCount = nodes.getLength();
			for (int i = 0; i < nodeCount; i++) {
				Node node = nodes.item(i);
				NamedNodeMap attributes = node.getAttributes();
				for (String attributeName : targetAttributes) {
					Node attribute = attributes.getNamedItem(attributeName);
					replaceFqVersion(attribute, newVersion, suffix);
				}
			}
			writeAsXML(doc, filename);
		} catch (Exception e) {
			reportError(filename, newVersion, e);
		}
	}

	private static void bumpTextTo(String filename, String regexp, String replacement, int numberOfMatchesNeeded) 
	{
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String line;
			int matches = 0;
			while ((line = reader.readLine()) != null) {
				if (matches < numberOfMatchesNeeded && line.matches(regexp)) {
					matches++;
					line = line.replaceFirst(regexp, replacement);
				}
				sb.append(line);
				sb.append("\n");
			}
			reader.close();
			writeFile(sb, filename);
		} catch (IOException e) {
			reportError(filename, regexp, e);
		}
	}
	
	private static void bumpTextsTo(String dirname, String extension, String regexp, String replacement, int i) {
		File dir = new File(dirname);
		String[] filenames = dir.list();
		for (String filename : filenames) {
			if (filename.endsWith(extension)) {
				bumpTextTo(dirname+File.separator+filename, regexp, replacement, i);
			}
		}
	}
	
	private static void replaceFqVersion(Node n, String version, String suffix) {
		String value = n.getNodeValue().toString();
		value = value.replaceFirst(DOTTED_VERSION_EXPR + suffix, version);
		n.setNodeValue(value);
	}

	private static Document openAsDOM(String filename) 
	throws SAXException, IOException, ParserConfigurationException 
	{
		File fXmlFile = new File(filename);
	    DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(fXmlFile);
		return doc;
	}

	private static void writeAsXML(Document doc, String filename) 
	throws TransformerException, FileNotFoundException 
	{
		System.out.println("Writing "+filename);
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(filename));
		transformer.transform(source, result);
		System.out.println("Done");
	}
	
	private static void writeFile(StringBuffer sb, String filename) 
	throws IOException 
	{
		System.out.println("Writing "+filename);
		FileWriter writer = new FileWriter(filename);
		writer.write(sb.toString());
		writer.close();
		System.out.println("Done");
	}

	private static void usageAndExit() {
		System.err.println("usage: VersionBumper <x.y.z>");
		System.exit(1);
	}

	private static void reportError(String filename, String newVersion, Exception e) {
		e.printStackTrace();
		System.err.println("Bumping failed: "+filename+" "+newVersion);
	}	
	
}
