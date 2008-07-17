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
 * $Id: Repository.java 175 2008-07-17 23:14:30Z shred $
 */

package org.shredzone.repowatch.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * Represents a repository.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 175 $
 */
@Entity
public class Repository extends BaseModel implements Serializable {
    private static final long serialVersionUID = 4678802328900240584L;
    
    private Domain domain;
    private String name;
    private String architecture;
    private String baseUrl;
    private String repoviewUrl;
    private Date firstScanned;
    private Date lastScanned;
    private long lastModified;
    
    /**
     * {@link Domain} this repository belongs to
     */
    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @Basic(fetch=FetchType.LAZY, optional=false)
    public Domain getDomain()               { return domain; }
    public void setDomain(Domain domain)    { this.domain = domain; }

    /**
     * Name of the repository. (e.g. "updates")
     */
    @Basic(optional=false)
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    /**
     * Architecture of the repository. (e.g. "i386")
     */
    @Basic(optional=false)
    public String getArchitecture()         { return architecture; }
    public void setArchitecture(String architecture) { this.architecture = architecture; }

    /**
     * Base URL of the repository itself.
     */
    @Basic(optional=false)
    public String getBaseUrl()              { return baseUrl; }
    public void setBaseUrl(String baseUrl)  { this.baseUrl = baseUrl; }

    /**
     * Base URL of repoview pages.
     */
    public String getRepoviewUrl()          { return repoviewUrl; }
    public void setRepoviewUrl(String repoviewUrl) { this.repoviewUrl = repoviewUrl; }

    /**
     * First time that repository was scanned.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFirstScanned()           { return firstScanned; }
    public void setFirstScanned(Date firstScanned) { this.firstScanned = firstScanned; }

    /**
     * Most recent time that repository was scanned.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastScanned()            { return lastScanned; }
    public void setLastScanned(Date lastScanned) { this.lastScanned = lastScanned; }

    /**
     * Time the primary.xml.gz was most recently changed. This is a long!
     */
    public long getLastModified()           { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

    /**
     * As {@link #getLastModified()}, but returning a {@link Date} object.
     */
    @Transient
    public Date getLastModifiedDate() {
        return new Date(getLastModified());
    }
    
    /**
     * Returns a human readable string for the repository.
     * 
     * @return  Human readable string, e.g. "<tt>fedora f7 updates (i386)</tt>".
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDomain().getName()).append(" ");
        sb.append(getDomain().getRelease()).append(" ");
        sb.append(getName()).append(" ");
        sb.append("(").append(getArchitecture()).append(")");
        return sb.toString();
    }
    
}
