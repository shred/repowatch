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
 * $Id: RepoMdParserTest.java 184 2008-07-23 22:57:56Z shred $
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
 * @version $Revision: 184 $
 */
public class RepoMdParserTest {

    @Test
    public void testParse() throws IOException {
        DatabaseLocation dbLocation;
        
        URL testfile = RepoMdParserTest.class.getResource("RepoMdParserTest.class");

        RepoMdParser parser = new RepoMdParser(testfile);
        parser.parse();
            
        dbLocation = parser.getDatabaseLocation("primary");
        assertNotNull(dbLocation);
        assertEquals(dbLocation.getType(), "primary");
        assertEquals(dbLocation.getLocation(), "repodata/primary.xml.gz");
        assertTrue(dbLocation.isCompressed());
        assertEquals(dbLocation.getChecksum(), "c225ad761f4299103a0b65fcb83bf986e5005bc5");
        assertEquals(dbLocation.getChecksumType(), "sha");
        assertEquals(dbLocation.getTimestamp(), 1216447007);
        assertEquals(dbLocation.getTimestampAsDate(), new Date(1216447007));

        dbLocation = parser.getDatabaseLocation("other");
        assertNotNull(dbLocation);
        assertEquals(dbLocation.getType(), "other");
        assertEquals(dbLocation.getLocation(), "repodata/other.xml");
        assertFalse(dbLocation.isCompressed());
        assertEquals(dbLocation.getChecksum(), "e85590ccf9b8777d2714ac7c62731d4e");
        assertEquals(dbLocation.getChecksumType(), "md5");
        assertEquals(dbLocation.getTimestamp(), 1216447007);
        assertEquals(dbLocation.getTimestampAsDate(), new Date(1216447007));
        
        dbLocation = parser.getDatabaseLocation("filelists");
        assertNotNull(dbLocation);
        assertEquals(dbLocation.getType(), "filelists");
        assertEquals(dbLocation.getLocation(), "repodata/filelists.xml.gz");
        assertTrue(dbLocation.isCompressed());
        assertNull(dbLocation.getChecksum());
        assertNull(dbLocation.getChecksumType());
        assertEquals(dbLocation.getTimestamp(), 1216447007);
        assertEquals(dbLocation.getTimestampAsDate(), new Date(1216447007));
        
        dbLocation = parser.getDatabaseLocation("foobar");
        assertNull(dbLocation);
    }

}
