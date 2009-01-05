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
 * $Id: SearchDTO.java 197 2008-07-28 22:31:00Z shred $
 */

package org.shredzone.repowatch.repository;

import java.io.Serializable;

import org.shredzone.repowatch.model.Domain;

/**
 * A <i>Data Transport Object</i> for search parameters.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 197 $
 */
public class SearchDTO implements Serializable {
    private static final long serialVersionUID = 1311197118497286370L;
    
    private String term;
    private int page;
    private boolean desc;
    private Domain domainOnly;

    /**
     * Gets the page currently shown in the search result.
     * 
     * @return Page number
     */
    public int getPage()                    { return page; }
    public void setPage(int page)           { this.page = page; }

    /**
     * Gets the search term.
     * 
     * @return Search term
     */
    public String getTerm()                 { return term; }
    public void setTerm(String term)        { this.term = term; }

    /**
     * Checks if the summary and description are to be searched as well.
     * 
     * @return true: also search in summary and description
     */
    public boolean isDescriptions()         { return desc; }
    public void setDescriptions(boolean desc) { this.desc = desc; }
   
    /**
     * Limits the search to the given {@link Domain}. If <code>null</code>,
     * all domains will be searched.
     * 
     * @return  {@link Domain} to limit the search to
     */
    public Domain getDomainOnly()           { return domainOnly; }
    public void setDomainOnly(Domain dom)   { this.domainOnly = dom; }
}
