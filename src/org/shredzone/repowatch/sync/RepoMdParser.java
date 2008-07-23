/* 
 * Repowatch -- A repository watcher
 *   (C) 2008 Richard "Shred" Körber
 *   http://repowatch.shredzone.org/
 *-----------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id: RepoMdParser.java 184 2008-07-23 22:57:56Z shred $
 */

package org.shredzone.repowatch.sync;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Parses the repomd.xml file of a repository.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 184 $
 */
public class RepoMdParser {

    private final Map<String,DatabaseLocation> databaseMap = new HashMap<String,DatabaseLocation>();
    private final URL baseUrl;

    // Element QName constants
    private final QName QN_DATA = new QName("http://linux.duke.edu/metadata/repo", "data");
    private final QName QN_LOCATION = new QName("http://linux.duke.edu/metadata/repo", "location");
    private final QName QN_CHECKSUM = new QName("http://linux.duke.edu/metadata/repo", "checksum");
    private final QName QN_TIMESTAMP = new QName("http://linux.duke.edu/metadata/repo", "timestamp");
    
    private final QName QNA_TYPE = new QName("type");
    private final QName QNA_HREF = new QName("href");

    /**
     * Creates a RepoMdParser. No network connection is opened yet.
     * 
     * @param baseUrl   Base URL of the repository.
     */
    public RepoMdParser(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Gets the {@link DatabaseLocation} for the given database type.
     * {@link #parse()} must be invoked once prior to this method call.
     * 
     * @param type  Database type (like "primary", "filelists", "other")
     * @return  {@link DatabaseLocation} to this database, or <code>null</code>
     *      if there was none of that type.
     */
    public DatabaseLocation getDatabaseLocation(String type) {
        return databaseMap.get(type);
    }
    
    /**
     * Parses the repomd file. This method creates an actual internet
     * connection and parses the XML file. It must be invoked exactly once
     * before {@link #getDatabaseLocation(String)}.
     * 
     * @throws IOException  The xml file could not be read or parsed for
     *  various reasons.
     */
    public void parse() throws IOException {
        databaseMap.clear();
        DatabaseLocation currentLocation = null;
        
        URL url = new URL(baseUrl, "repodata/repomd.xml");
        InputStream in = url.openStream();
        
        try {
            XMLInputFactory xmlfactory = XMLInputFactory.newInstance();
            XMLEventReader xmlreader = xmlfactory.createXMLEventReader(in);
            StringBuilder stringbuilder = null;
            
            while (xmlreader.hasNext()) {
                XMLEvent event = xmlreader.nextEvent();
                
                if (event.isStartElement()) {
                    StartElement element = (StartElement) event;
                    QName tag = element.getName();
                    
                    if (tag.equals(QN_DATA)) {
                        Attribute attr = element.getAttributeByName(QNA_TYPE);
                        if (attr != null) {
                            assert currentLocation == null;
                            currentLocation = new DatabaseLocation();
                            currentLocation.setType(attr.getValue());
                        }
                        
                    } else if (tag.equals(QN_LOCATION)) {
                        Attribute attr = element.getAttributeByName(QNA_HREF);
                        if (currentLocation != null && attr != null) {
                            String href = attr.getValue();
                            currentLocation.setLocation(href);
                            currentLocation.setCompressed(href.endsWith(".gz"));
                        }
                        
                    } else if (tag.equals(QN_CHECKSUM)) {
                        Attribute attr = element.getAttributeByName(QNA_TYPE);
                        if (currentLocation != null && attr != null) {
                            currentLocation.setChecksumType(attr.getValue());
                        }
                        assert stringbuilder == null;
                        stringbuilder = new StringBuilder();
                        
                    } else if (tag.equals(QN_TIMESTAMP)) {
                        assert stringbuilder == null;
                        stringbuilder = new StringBuilder();
                    }
                    
                } else if (event.isEndElement() && currentLocation != null) {
                    EndElement element = (EndElement) event;
                    QName tag = element.getName();
                    
                    if (tag.equals(QN_DATA)) {
                        databaseMap.put(currentLocation.getType(), currentLocation);
                        currentLocation = null;
                        
                    } else if (tag.equals(QN_CHECKSUM)) {
                        assert stringbuilder != null;
                        currentLocation.setChecksum(stringbuilder.toString().trim());
                        stringbuilder = null;
                        
                    } else if (tag.equals(QN_TIMESTAMP)) {
                        assert stringbuilder != null;
                        currentLocation.setTimestamp(Long.parseLong(stringbuilder.toString().trim()));
                        stringbuilder = null;
                    }
                    
                } else if (event.isCharacters() && stringbuilder != null) {
                    Characters characters = (Characters) event;
                    stringbuilder.append(characters.getData());
                }
            }
        
        } catch (XMLStreamException ex) {
            throw (IOException) new IOException("Could not read repomd.xml").initCause(ex);
        } finally {
            in.close();
        }
    }
}
