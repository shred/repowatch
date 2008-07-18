package org.shredzone.repowatch.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Note that Spring is about to bring a similar functionality, see
 * http://jira.springframework.org/browse/SPR-4451 . If it's there,
 * this class should be removed.
 */
public class RequestMappingResolver {
    /*TODO: Check if this class can be removed because Spring brings
     * its functionality.
     */
    
    private final Pattern pattern;
    
    public RequestMappingResolver(String url) {
        pattern = Pattern.compile(url.replace("*", "(.*?)"));
    }
    
    public RequestParts getRequestParts(HttpServletRequest req) {
        String context = req.getContextPath();
        String request = req.getRequestURI();
        if (request.startsWith(context)) {
            request = request.substring(context.length());
        }
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
    
    public static class RequestParts {
        private final String[] parts;
        
        public RequestParts(String[] parts) {
            this.parts = parts;
        }
        
        public boolean hasParts() {
            return parts != null;
        }
        
        public int partCount() {
            return parts.length;
        }
        
        public String getPart(int index) {
            return parts[index];
        }
    }
    
}
