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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

/**
 * Represents a package. A package is unique within a domain, but can have
 * different versions in the repositories of that domain.
 *
 * @author Richard "Shred" Körber
 */
@Entity
public class Package extends BaseModel {
    private static final long serialVersionUID = 5899312192749814283L;

    private Domain domain;
    private String name;
    private String summary;
    private String description;
    private String packGroup;
    private String homeUrl;

    /**
     * {@link Domain} this package belongs to.
     */
    @ManyToOne
    @Basic(fetch=FetchType.LAZY, optional=false)
    public Domain getDomain()               { return domain; }
    public void setDomain(Domain domain)    { this.domain = domain; }

    /**
     * Name of the package.
     */
    @Basic(optional=false)
    @Index(name="nameindex")
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    /**
     * A short summary of the package's functionality.
     */
    @Lob
    public String getSummary()              { return summary; }
    public void setSummary(String summary)  { this.summary = summary; }

    /**
     * A detailed description of the package's functionality.
     */
    @Lob
    public String getDescription()          { return description; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Home page of the package. (e.g. "http://www.gnu.org")
     */
    public String getHomeUrl()              { return homeUrl; }
    public void setHomeUrl(String homeUrl)  { this.homeUrl = homeUrl; }

    /**
     * Package group this package belongs to.
     */
    public String getPackGroup()            { return packGroup; }
    public void setPackGroup(String packGroup) { this.packGroup = packGroup; }

}
