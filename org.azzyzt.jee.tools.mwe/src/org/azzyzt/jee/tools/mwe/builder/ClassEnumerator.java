/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.tools.mwe.builder;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.azzyzt.jee.tools.mwe.exception.ToolError;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ClassEnumerator {

    public static final String PERSISTENCE_UNIT_WILDCARD = "*";

    private String persistenceUnitName = PERSISTENCE_UNIT_WILDCARD;

    public ClassEnumerator (String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public List<String> getPersistentClassNames() {
        List<String> classes = new ArrayList<String>();
        String resourceName = "META-INF/persistence.xml";
        List<URL> resourcesFound = locateResources(resourceName);
        for (URL u : resourcesFound) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
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

                for (int i = 0; i < nodes.getLength(); i++) {
                    classes.add(nodes.item(i).getNodeValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ToolError(e);
            }
        }
        return classes;
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
