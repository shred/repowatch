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
 * $Id: PrimaryParserTest.java 185 2008-07-24 12:04:15Z shred $
 */

package org.shredzone.repowatch.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.shredzone.repowatch.model.Domain;
import org.shredzone.repowatch.model.Package;
import org.shredzone.repowatch.model.Repository;
import org.shredzone.repowatch.model.Version;
import org.shredzone.repowatch.service.SynchronizerException;

/**
 * Unit tests for {@link PrimaryParser}
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 185 $
 */
public class PrimaryParserTest {

    private Domain domain;
    private Repository repository;
    private DatabaseLocation dbLoc;

    @Before
    public void initialize() {
        domain = createMockDomain();
        repository = createMockRepository();
        dbLoc = createMockDatabaseLocation();
    }

    private Domain createMockDomain() {
        Domain dom = new Domain();
        dom.setId(1);
        dom.setName("mocktux");
        dom.setRelease("development");
        return dom;
    }
    
    private Repository createMockRepository() {
        Repository repo = new Repository();
        repo.setId(2);
        repo.setDomain(domain);
        repo.setBaseUrl(RepoMdParserTest.class.getResource("PrimaryParserTest.class").toExternalForm());
        repo.setName("updates");
        repo.setArchitecture("i386");
        return repo;
    }
    
    private DatabaseLocation createMockDatabaseLocation() {
        DatabaseLocation loc = new DatabaseLocation();
        loc.setLocation("repodata/primary.xml.gz");
        loc.setCompressed(true);
        loc.setChecksumType("sha");
        /*TODO: dynamically compute the checksum at compile time or run time! */
        loc.setChecksum("3e63bf9ece3c0c0a6b322ffdebe26e6a6b0a9d3c");
        return loc;
    }
    
    @Test
    public void testPrimaryParser() {
        PrimaryParser parser = new PrimaryParser(repository, dbLoc);
        try {
            Version version;

            parser.parse();
            
            version = parser.readNextVersion();
            assertNotNull(version);
            checkVersion(version);
            
            version = parser.readNextVersion();
            assertNull(version);
            
        } catch (SynchronizerException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            parser.discard();
        }
    }
    
    @Test
    public void testPrimaryParserIterator() {
        PrimaryParser parser = new PrimaryParser(repository, dbLoc);
        try {
            parser.parse();
            
            int ix = 0;
            for (Version version : parser) {
                assertNotNull(version);
                checkVersion(version);
                ix++;
            }
            assertEquals(1, ix);
            
        } catch (SynchronizerException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            parser.discard();
        }
    }

    @Test
    public void testPrimaryParserBadChecksum() {
        String oldSum = dbLoc.getChecksum();
        dbLoc.setChecksum("badcafef9ec2ffde6e6a6b0a9dbe2e3c0c0a6b32"); // wrong sum
        PrimaryParser parser = new PrimaryParser(repository, dbLoc);
        try {
            parser.parse();
            
            assertNotNull(parser.readNextVersion());
            assertNull(parser.readNextVersion());
            fail("Bad checksum was not detected!");
            
        } catch (SynchronizerException ex) {
            if ("Bad digest checksum!".equals(ex.getMessage())) {
                // Bad checksum was discovered. Success!
            } else {
                ex.printStackTrace();
                fail("Unexpected exception");
            }
        } finally {
            parser.discard();
            dbLoc.setChecksum(oldSum);
        }
    }

    private void checkVersion(Version version) {
        assertNotNull(version);
        Package pack = version.getPackage();
        assertNotNull(pack);
        
        assertSame(domain, pack.getDomain());
        assertEquals("foo", pack.getName());
        assertEquals("A very special software", pack.getSummary());
        assertEquals("Foo is a very\nspecial software package.", pack.getDescription());
        assertEquals("http://www.foo.example/foo.html", pack.getHomeUrl());
        assertEquals("System Environment/Libraries", pack.getPackGroup());
        
        assertSame(repository, version.getRepository());
        assertEquals("0", version.getEpoch());
        assertEquals("1.2.3", version.getVer());
        assertEquals("4.mock", version.getRel());
        assertEquals(new Date(1214421558 * 1000L), version.getFileDate());
        assertEquals("foo-1.2.3-4.mock.i386.rpm", version.getFileLocation());
    }

}
