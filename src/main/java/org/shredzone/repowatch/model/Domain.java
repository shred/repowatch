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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents a domain. A domain is a project or a site that offers one or
 * more repositories which logically belong together. E.g. "Fedora Project"
 * or "Freshrpms".
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
@Entity
public class Domain extends BaseModel {
    private static final long serialVersionUID = 4370772533550604837L;
    
    private String name;
    private String release;
    private String homeUrl;
    
    /**
     * Name of the domain. (e.g. "fedora")
     */
    @Basic(optional=false)
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    /**
     * Release of that domain. (e.g. "f7")
     */
    @Column(name="rele")
    @Basic(optional=false)
    public String getRelease()              { return release; }
    public void setRelease(String release)  { this.release = release; }

    /**
     * Home URL of that domain.
     */
    public String getHomeUrl()              { return homeUrl; }
    public void setHomeUrl(String homeUrl)  { this.homeUrl = homeUrl; }

}
