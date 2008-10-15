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
 * $Id: Domain.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Represents a blacklist entry. Packages with a name in this blacklist
 * will never show up on the web site.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@Entity
public class Blacklist extends BaseModel {
    private static final long serialVersionUID = 2177832459706690711L;
    
    private String name;
    
    /**
     * Name of the blacklisted package.
     */
    @Basic(optional=false)
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

}
