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

package org.shredzone.repowatch.web.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.shredzone.repowatch.web.util.RequestMappingResolver.RequestParts;

/**
 * Unit tests for {@link RequestMappingResolver}
 *
 * @author Richard "Shred" Körber
 */
public class RequestMappingResolverTest {

    @Test
    public void testGetRequestParts() {
        RequestMappingResolver resolver;
        RequestParts parts;

        //--- Mappings without wildcard ---
        resolver = new RequestMappingResolver("/foo.html");
        parts = resolver.getRequestParts("/foo.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/bar.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        //--- Mappings with single wildcard ---
        resolver = new RequestMappingResolver("/foo/*.html");
        parts = resolver.getRequestParts("/foo.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/foo/abc.html");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc", parts.getPart(0));

        parts = resolver.getRequestParts("/foo/abc.png");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/foo/abc/def.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/bar/foo/abc.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        //--- Mappings with multiple wildcards ---
        resolver = new RequestMappingResolver("/foo/*/*.html");
        parts = resolver.getRequestParts("/foo.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/foo/abc.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/foo/abc/def.html");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(2, parts.partCount());
        assertEquals("abc", parts.getPart(0));
        assertEquals("def", parts.getPart(1));

        //--- Mapping with trailing wildcard ---
        resolver = new RequestMappingResolver("/foo/*");
        parts = resolver.getRequestParts("/foo.html");
        assertNotNull(parts);
        assertFalse(parts.hasParts());
        assertEquals(0, parts.partCount());

        parts = resolver.getRequestParts("/foo/abc.html");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc.html", parts.getPart(0));

        parts = resolver.getRequestParts("/foo/abc.gif");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc.gif", parts.getPart(0));

        //--- Requests with sessions, parameters and anchors ---
        resolver = new RequestMappingResolver("/foo/*.html");
        parts = resolver.getRequestParts("/foo/abc.html;jsessionid=12345678");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc", parts.getPart(0));

        parts = resolver.getRequestParts("/foo/abc.html?id=1234;follow=site.html");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc", parts.getPart(0));

        parts = resolver.getRequestParts("/foo/abc.html#anchor");
        assertNotNull(parts);
        assertTrue(parts.hasParts());
        assertEquals(1, parts.partCount());
        assertEquals("abc", parts.getPart(0));
    }
}
