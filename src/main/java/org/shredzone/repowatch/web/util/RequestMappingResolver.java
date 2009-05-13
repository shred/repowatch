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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Resolves the URL according to the {@link RequestMapping} pattern.
 * <p>
 * Note that Spring is about to bring a similar functionality, see
 * <href="http://jira.springframework.org/browse/SPR-4451">here</a> .
 * If it is available, this class should be removed and replaced by
 * the official solution.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class RequestMappingResolver {
    /*TODO: Check if this class can be removed because Spring brings
     * its functionality.
     */
    
    private final Pattern pattern;

    /**
     * Creates a new RequestMappingResolver.
     * 
     * @param url       The URL given to the corresponding
     *      {@link RequestMapping} annotation.
     */
    public RequestMappingResolver(String url) {
        String regexp = url.replace("*", "([^/]*?)");
        regexp += "(?:[?#;].*)?";       // Ignore jsession attachments and more
        pattern = Pattern.compile(regexp);
    }
    
    /**
     * Gets the {@link RequestParts} object for the given request.
     * 
     * @param req   {@link HttpServletRequest}
     * @return  {@link RequestParts} with the results
     */
    public RequestParts getRequestParts(HttpServletRequest req) {
        String context = req.getContextPath();
        String request = req.getRequestURI();
        
        if (request.startsWith(context)) {
            // Remove the context path
            request = request.substring(context.length());
        }
        
        return getRequestParts(request);
    }

    /**
     * Gets the {@link RequestParts} object for the given request.
     * 
     * @param request   Request part (requestURI minus contextPath).
     * @return  {@link RequestParts} with the results
     */
    public RequestParts getRequestParts(String request) {
        Matcher m = pattern.matcher(request);
        if (m.matches()) {
            String[] result = new String[m.groupCount()];
            for (int ix = 0; ix < result.length; ix++) {
                result[ix] = m.group(ix + 1);
            }
            return new RequestParts(result);
        } else {
            return new RequestParts(null);
        }
    }

    /**
     * This class contains the single parts of a resolved URL.
     */
    public static class RequestParts {
        private final String[] parts;
        
        /**
         * Creates a new RequestParts object.
         * 
         * @param parts     The single request parts.
         */
        public RequestParts(String[] parts) {
            this.parts = parts;
        }

        /**
         * Checks if this RequestParts contains any parts. If false, the
         * URL usually did not match the {@link RequestMapping} pattern.
         * 
         * @return  true: Request matched, false: Request did not match
         */
        public boolean hasParts() {
            return partCount() > 0;
        }

        /**
         * Counts the number of parts.
         * 
         * @return  Number of parts
         */
        public int partCount() {
            return (parts != null ? parts.length : 0);
        }

        /**
         * Gets the part with a given index. This is the number of the
         * desired '*' in the {@link RequestMapping} pattern, starting
         * from 0.
         * 
         * @param index     Number of part, starting from 0.
         * @return      Part string.
         */
        public String getPart(int index) {
            return parts[index];
        }
    }
    
}
