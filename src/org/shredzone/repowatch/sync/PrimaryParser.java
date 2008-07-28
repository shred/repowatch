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
 * $Id: PrimaryParser.java 196 2008-07-28 22:30:21Z shred $
 */

package org.shredzone.repowatch.sync;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.service.SynchronizerException;

/**
 * This beast parses a <tt>primary.xml</tt> file. It also takes care for
 * gunzipping the file and computing the checksum, if desired. The single
 * {@link Version} objects found in the xml file are available via 
 * {@link #readNextVersion()}, but this class also implements the
 * {@link Iterable} interface, so it can be simply used in a for loop.
 * <p>
 * Invoke {@link #parse()} before reading the content. The xml file is
 * processed on the fly, so the memory footprint is rather small even for
 * large xml files. <em>Always</em> invoke {@link #discard()} after processing
 * or when an exception occured!
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 196 $
 */
public class PrimaryParser implements Iterable<Version> {

    // Element QName constants
    private final QName QN_PACKAGE = new QName("http://linux.duke.edu/metadata/common", "package");
    private final QName QN_VERSION = new QName("http://linux.duke.edu/metadata/common", "version");
    private final QName QN_TIME = new QName("http://linux.duke.edu/metadata/common", "time");
    private final QName QN_LOCATION = new QName("http://linux.duke.edu/metadata/common", "location");
    private final QName QN_NAME = new QName("http://linux.duke.edu/metadata/common", "name");
    private final QName QN_SUMMARY = new QName("http://linux.duke.edu/metadata/common", "summary");
    private final QName QN_DESCRIPTION = new QName("http://linux.duke.edu/metadata/common", "description");
    private final QName QN_URL = new QName("http://linux.duke.edu/metadata/common", "url");
    private final QName QN_RPM_GROUP = new QName("http://linux.duke.edu/metadata/rpm", "group");
 
    private final QName QNA_EPOCH = new QName("epoch");
    private final QName QNA_VER = new QName("ver");
    private final QName QNA_REL = new QName("rel");
    private final QName QNA_FILE = new QName("file");
    private final QName QNA_HREF = new QName("href");
    
    private final DatabaseLocation location;
    private final Repository repository;
    
    private InputStream in;
    private MessageDigest digest;
    private XMLEventReader xmlreader;
    private List<StringBuilder> stringStack = new LinkedList<StringBuilder>();
    private Package currentPackage;
    private Version currentVersion;
    
    /**
     * Creates a new PrimaryParser. The parser is just created, no network
     * connection is opened yet.
     * 
     * @param repository    {@link Repository} to parse
     * @param location      {@link DatabaseLocation} with details for the
     *      database to be read.
     */
    public PrimaryParser(Repository repository, DatabaseLocation location) {
        this.repository = repository;
        this.location = location;
    }
    
    /**
     * Parses the database. A network connection is opened and the xml file
     * is read and processed. The content is available by invoking
     * {@link #readNextVersion()} or getting an interator by {@link #iterator()}.
     * <p>
     * <em>Always invoke {@link #discard()} after completion!</em> Also invoke
     * {@link #discard()} if an exception occured.
     * 
     * @throws SynchronizerException  An error occured while parsing.
     */
    public void parse() throws SynchronizerException {
        try {
            URL baseUrl = new URL(repository.getBaseUrl());
            URL url = new URL(baseUrl, location.getLocation());
            in = url.openStream();
            if (location.getChecksumType() != null) {
                digest = MessageDigest.getInstance(location.getChecksumType().toUpperCase());
                in = new DigestInputStream(in, digest);
            }
            if (location.isCompressed()) {
                in = new GZIPInputStream(in);
            }
            XMLInputFactory xmlfactory = XMLInputFactory.newInstance();
            xmlreader = xmlfactory.createXMLEventReader(in);
        } catch (IOException ex) {
            throw new SynchronizerException("Could not read primary.xml", ex);
        } catch (XMLStreamException ex) {
            throw new SynchronizerException("Could not parse primary.xml", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SynchronizerException("Could not compute checksum", ex);
        }
    }
    
    /**
     * Reads the next {@link Version} entity found in the xml file. If there
     * are no more entries, <code>null</code> is returned.
     * <p>
     * Note that a bad checksum is only reported after the last entry was read.
     * 
     * @return  Next {@link Version} entity that was found, or <code>null</code>
     *   if the last entity was processed.
     * @throws SynchronizerException  An error occured while reading.
     */
    public Version readNextVersion() throws SynchronizerException {
        try {
            while (xmlreader.hasNext()) {
                XMLEvent event = xmlreader.nextEvent();
                
                if (event.isStartElement()) {
                    stringStack.add(0, new StringBuilder());
                    
                    StartElement element = (StartElement) event;
                    parseStartElement(element, element.getName());
 
                } else if (event.isEndElement()) {
                    String str = stringStack.remove(0).toString().trim();
                    
                    EndElement element = (EndElement) event;
                    Version v = parseEndElement(element, element.getName(), str);
                    if (v != null) return v;
                    
                } else if (event.isCharacters()) {
                    Characters characters = (Characters) event;
                    if (! stringStack.isEmpty()) {
                        stringStack.get(0).append(characters.getData());
                    }

                }
            }
        } catch (XMLStreamException ex) {
            throw new SynchronizerException("Reading primary.xml", ex);
        }

        if (digest != null) {
            byte[] hash = digest.digest();
            String cmp = location.getChecksum().toLowerCase();
            if (hash.length * 2 != cmp.length()) {
                throw new SynchronizerException("Bad digest checksum!");
            }
            
            for (int ix = 0; ix < hash.length; ix++) {
                String hex = String.format("%02x", hash[ix]);
                if (! cmp.startsWith(hex)) {
                    throw new SynchronizerException("Bad digest checksum!");
                }
                cmp = cmp.substring(2);
            }
            assert cmp.length() == 0;
        }
        
        return null;
    }
    
    /**
     * Frees all resources bound by the parser. Always invoke this method
     * when you are done reading the xml file. You can safely invoke it
     * multiple times.
     */
    public void discard() {
        try {
            if (in != null) in.close();
        } catch (IOException ex) {
            throw new RuntimeException("While discarding", ex);
        } finally {
            in = null;
            xmlreader = null;
            currentPackage = null;
            currentVersion = null;
            digest = null;
            stringStack.clear();
        }
    }
    
    /**
     * A starting element was found in the XML stream.
     * 
     * @param element   Element that was found
     * @param tag       QName of the element's tag.
     * @throws XMLStreamException
     */
    private void parseStartElement(StartElement element, QName tag)
    throws XMLStreamException {
        if (tag.equals(QN_PACKAGE)) {
            assert currentPackage == null;
            currentPackage = new Package();
            currentPackage.setDomain(repository.getDomain());
            
            assert currentVersion == null;
            currentVersion = new Version();
            currentVersion.setRepository(repository);
            currentVersion.setPackage(currentPackage);
        } else if (tag.equals(QN_VERSION)) {
            assert currentVersion != null;
            
            Attribute attr = element.getAttributeByName(QNA_EPOCH);
            if (attr != null) currentVersion.setEpoch(attr.getValue());
            attr = element.getAttributeByName(QNA_VER);
            if (attr != null) currentVersion.setVer(attr.getValue());
            attr = element.getAttributeByName(QNA_REL);
            if (attr != null) currentVersion.setRel(attr.getValue());

        } else if (tag.equals(QN_TIME)) {
            Attribute attr = element.getAttributeByName(QNA_FILE);
            if (attr != null) {
                try {
                    long filetime = Long.parseLong(attr.getValue());
                    currentVersion.setFileDate(new Date(filetime * 1000L));
                } catch (NumberFormatException ex) {}
            }

        } else if (tag.equals(QN_LOCATION)) {
            Attribute attr = element.getAttributeByName(QNA_HREF);
            if (attr != null) currentVersion.setFileLocation(attr.getValue());
        }
    }
    
    /**
     * An ending element was found in the XML stream.
     * 
     * @param element   Element that was found
     * @param tag       QName of the element's tag.
     * @param body      Text found in the element's body.
     * @throws XMLStreamException
     */
    private Version parseEndElement(EndElement element, QName tag, String body)
    throws XMLStreamException {
        if (currentPackage == null) return null;
        
        if (tag.equals(QN_PACKAGE)) {
            Version result = currentVersion;
            currentPackage = null;
            currentVersion = null;
            return result;
            
        } else if (tag.equals(QN_NAME)) {
            currentPackage.setName(body);
            
        } else if (tag.equals(QN_SUMMARY)) {
            currentPackage.setSummary(body);
            
        } else if (tag.equals(QN_DESCRIPTION)) {
            currentPackage.setDescription(body);
            
        } else if (tag.equals(QN_URL)) {
            currentPackage.setHomeUrl(body);
            
        } else if (tag.equals(QN_RPM_GROUP)) {
            currentPackage.setPackGroup(body);
        }
        
        return null;
    }
    
    /**
     * Returns an iterator over the {@link Version} entities found.
     * {@link #parse()} must be invoked prior to this method.
     * 
     * @return Iterator
     */
    public Iterator<Version> iterator() {
        return new Iterator<Version>() {
            private boolean retrieved = true;
            private Version currentVersion = null;
            
            @Override
            public boolean hasNext() {
                if (retrieved) {
                    retrieved = false;
                    try {
                        currentVersion = readNextVersion();
                    } catch (SynchronizerException ex) {
                        throw new RuntimeException("While iterating XML", ex);
                    }
                }
                return currentVersion != null;
            }

            @Override
            public Version next() {
                if (hasNext()) {
                    retrieved = true;
                    return currentVersion;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
