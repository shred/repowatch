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
 
package org.shredzone.repowatch.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link BaseModel} and its derivates.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class ModelTester {

    @Test
    public void testEqualsObject() {
        Domain d1 = new Domain();
        d1.setId(1);
        d1.setName("Mocktux");
        d1.setRelease("development");
        
        Domain d2 = new Domain();
        d2.setId(1);                // Same ID as d1!
        d2.setName("Mocktux");
        d2.setRelease("development");
        
        Domain d3 = new Domain();
        d3.setId(2);
        d3.setName("Mocktux");
        d3.setRelease("release");
        
        Repository r1 = new Repository();
        r1.setId(1);                // Same ID as d1!
        r1.setDomain(d1);
        r1.setName("release");
        r1.setArchitecture("i386");
        
        Repository r2 = new Repository();
        r2.setId(2);
        r2.setDomain(d2);
        r2.setName("release");
        r2.setArchitecture("x86_64");
        
        // A Domain is always equal to itself
        assertTrue(d1.equals(d1));
        
        // Two Domains are also equal if their IDs are equal
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));
        
        // Two Domains are different if they have different IDs
        assertFalse(d1.equals(d3));
        assertFalse(d3.equals(d1));
        
        // A Repository is always equal to itself
        assertTrue(r1.equals(r1));
        
        // Two Repositories are different if they have different IDs
        assertFalse(r1.equals(r2));
        assertFalse(r2.equals(r1));
        
        // A Domain and a Repository are not equal even if the IDs are the same
        assertFalse(d1.equals(r1));
        assertFalse(r1.equals(d1));
        
        // A Domain and a Repository are not equal with different IDs
        assertFalse(d1.equals(r2));
        assertFalse(r2.equals(d1));
        
        // equals(null) always returns false
        assertFalse(d1.equals(null));
        
        // Comparisons with other objects always return false
        String foo = "1";
        assertFalse(d1.equals(foo));
        
    }
}
