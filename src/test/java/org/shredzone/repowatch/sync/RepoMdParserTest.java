/**
 * repowatch - A yum repository watcher
 *
 * Copyright (C) 2008 Richard "Shred" Körber
 *   http://repowatch.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.shredzone.repowatch.sync;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

/**
 * Unit tests for {@link RepoMdParser}
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class RepoMdParserTest {

    @Test
    public void testParse() throws IOException {
        DatabaseLocation dbLocation;
        
        URL testfile = RepoMdParserTest.class.getResource("/");

        RepoMdParser parser = new RepoMdParser(testfile);
        parser.parse();
            
        dbLocation = parser.getDatabaseLocation("primary");
        assertNotNull(dbLocation);
        assertEquals("primary", dbLocation.getType());
        assertEquals("repodata/primary.xml.gz", dbLocation.getLocation());
        assertTrue(dbLocation.isCompressed());
        assertEquals("c225ad761f4299103a0b65fcb83bf986e5005bc5", dbLocation.getChecksum());
        assertEquals("sha", dbLocation.getChecksumType());
        assertEquals(1216447007000L, dbLocation.getTimestamp());
        assertEquals(new Date(1216447007000L), dbLocation.getTimestampAsDate());

        dbLocation = parser.getDatabaseLocation("other");
        assertNotNull(dbLocation);
        assertEquals("other", dbLocation.getType());
        assertEquals("repodata/other.xml", dbLocation.getLocation());
        assertFalse(dbLocation.isCompressed());
        assertEquals("e85590ccf9b8777d2714ac7c62731d4e", dbLocation.getChecksum());
        assertEquals("md5", dbLocation.getChecksumType());
        assertEquals(1216447007000L, dbLocation.getTimestamp());
        assertEquals(new Date(1216447007000L), dbLocation.getTimestampAsDate());
        
        dbLocation = parser.getDatabaseLocation("filelists");
        assertNotNull(dbLocation);
        assertEquals("filelists", dbLocation.getType());
        assertEquals("repodata/filelists.xml.gz", dbLocation.getLocation());
        assertTrue(dbLocation.isCompressed());
        assertNull(dbLocation.getChecksum());
        assertNull(dbLocation.getChecksumType());
        assertEquals(1216447007000L, dbLocation.getTimestamp());
        assertEquals(new Date(1216447007000L), dbLocation.getTimestampAsDate());
        
        dbLocation = parser.getDatabaseLocation("foobar");
        assertNull(dbLocation);
    }

}
