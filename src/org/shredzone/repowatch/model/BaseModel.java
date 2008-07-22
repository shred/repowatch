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
 * $Id: BaseModel.java 181 2008-07-22 11:35:11Z shred $
 */

package org.shredzone.repowatch.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Represents the superclass of all other models. It contains the
 * primary key.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 181 $
 */
@MappedSuperclass
public abstract class BaseModel implements Serializable {
    protected long id;

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    public long getId()                     { return id; }
    public void setId(long id)              { this.id = id; }

    /**
     * Compares two model entities. The entities are considered equal if they
     * are of the same type and contain the same primary key ID.
     * 
     * @param   obj     Object to compare with
     * @return  true if the entities are deemed to be equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (! this.getClass().isAssignableFrom(obj.getClass())) return false;

        assert obj instanceof BaseModel;
        
        return ((BaseModel) obj).getId() == this.getId();
    }

    /**
     * Creates a hash code for this model entity.
     * 
     * @return      hash code
     */
    @Override
    public int hashCode() {
        return (int) ((getId()>>32) ^ getId());
    }

}
