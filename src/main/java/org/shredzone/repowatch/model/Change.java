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
 * $Id: Change.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;

/**
 * Represents a changelog entry.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@Entity
@Table(name="Changelog")
public class Change extends BaseModel {
    private static final long serialVersionUID = 8648618861114240913L;
    
    public static enum Type { ADDED, UPDATED, REMOVED };
    
    private Date timestamp = new Date();
    private Repository repo;
    private Package pack;
    private String epoch;
    private String ver;
    private String rel;
    private Type change = Type.ADDED;

    /**
     * Timestamp of the event.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ts")      // "timestamp" would be an invalid column name!
    @Basic(optional=false)
    public Date getTimestamp()              { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    /**
     * {@link Repository} where the change took place.
     */
    @ManyToOne
    @Basic(fetch=FetchType.LAZY, optional=false)
    public Repository getRepository()          { return repo; }
    public void setRepository(Repository repo) { this.repo = repo; }

    /**
     * {@link Package} that was changed.
     */
    @ManyToOne
    @Basic(fetch=FetchType.LAZY, optional=false)
    public Package getPackage()             { return pack; }
    public void setPackage(Package pack)    { this.pack = pack; }

    /**
     * Epoch of the {@link Package} at the time of that change.
     */
    public String getEpoch()                { return epoch; }
    public void setEpoch(String epoch)      { this.epoch = epoch; }

    /**
     * Version of the {@link Package} at the time of that change.
     */
    public String getVer()                  { return ver; }
    public void setVer(String ver)          { this.ver = ver; }

    /**
     * Release of the {@link Package} at the time of that change.
     */
    public String getRel()                  { return rel; }
    public void setRel(String rel)          { this.rel = rel; }

    /**
     * Change type.
     * 
     * @see Type
     */
    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    @Index(name="typeindex")
    public Type getChange()                 { return change; }
    public void setChange(Type change)      { this.change = change; }


}
