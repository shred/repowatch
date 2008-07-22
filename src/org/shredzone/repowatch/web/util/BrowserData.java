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
 * $Id: BrowserData.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.web.util;

/**
 * Contains all data required to show a page browser.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
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
    public int getPagecount() { return pagecount; }
    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
        if (page >= pagecount) page = pagecount-1;
    }

    /**
     * Gets a good guess for large step increments. This implementation depends
     * on the current pagecount and always returns a decimal power in the
     * magnitude of the pagecount.
     * <p>
     * Examples: pagecount=14, pagesteps=10. pagecount=321, pagesteps=100.
     * pagecount=812312, pagesteps=100000.
     * <p>
     * For a pagecount of 0, 0 is be returned.
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
    public int getPage() { return page; }
    public void setPage(int page) {
        if (page >= pagecount) {
            page = pagecount - 1;
        }
        if (page < 0) {
            page = 0;
        }
        this.page = page;
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
