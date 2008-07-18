/* 
 * Repowatch -- A repository watcher
 *   (C) 2007 Richard "Shred" Körber
 *   http://www.shredzone.net/go/repowatch
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
 * $Id: BrowserData.java 177 2008-07-18 20:56:18Z shred $
 */

package org.shredzone.repowatch.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains all data required to show a page browser.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 177 $
 */
public class BrowserData {
    private int pagecount;
    private int page;
    private long resultcount;
    private boolean dotted;
    private String baseurl;
    private String pageparam = "page";

    /**
     * Gets the number of pages to be shown.
     * 
     * @return Number of pages
     */
    public int getPagecount()                   { return pagecount; }
    public void setPagecount(int pagecount)     { this.pagecount = pagecount; }

    /**
     * Gets a good guess for large step increments. This implementation depends
     * on the current pagecount and always returns a decimal power in the
     * magnitude of the pagecount.
     * <p>
     * Examples: pagecount=14, pagesteps=10. pagecount=321, pagesteps=100.
     * pagecount=812312, pagesteps=100000.
     * <p>
     * For a pagecount of 0, 0 will also be returned.
     * 
     * @return Large step increments.
     */
    public int getPagesteps() {
        if (getPagecount() == 0) return 0;
        return (int) Math.pow(10, Math.floor(Math.log10((double)getPagecount())));
    }

    /**
     * Gets the page currently displayed.
     * 
     * @return Page number currently displayed.
     */
    public int getPage()                        { return page; }
    public void setPage(int page)               { this.page = page; }

    /**
     * Sets the page number dependent on the page parameter of the
     * {@link HttpServletRequest}.
     * 
     * @param req {@link HttpServletRequest} to get the page number from.
     */
    public void setPage(HttpServletRequest req) {
        page = 0;
        String param = req.getParameter(getPageparam());
        if (param != null) {
            try {
                page = Integer.parseInt(param);
            } catch (NumberFormatException ex) {
                page = 0;       // make no fuss about it...
            }
        }
        if (page >= pagecount) {
            page = pagecount - 1;
        }
    }

    /**
     * Gets the number of entries of the result. This is only used as
     * information to the user, and has no direct impact to the browser.
     *  
     * @return Number of entries
     */
    public long getResultcount()                 { return resultcount; }
    public void setResultcount(long resultcount) { this.resultcount = resultcount; }

    /**
     * Internal flag for the browser. Do not use.
     */
    public boolean isDotted()                   { return dotted; }
    public void setDotted(boolean dotted)       { this.dotted = dotted; }

    /**
     * Returns the URL to be loaded when another page is to be shown. This is
     * usually the URL of the page currently shown.
     * 
     * @return Base URL
     */
    public String getBaseurl()                  { return baseurl; }
    public void setBaseurl(String baseurl)      { this.baseurl = baseurl; }
    
    /**
     * Returns the parameter name containing the page currently shown in
     * this browser. Usually "page".
     * 
     * @return Page parameter name
     */
    public String getPageparam()                { return pageparam; }
    public void setPageparam(String pageparam)  { this.pageparam = pageparam; }

}
