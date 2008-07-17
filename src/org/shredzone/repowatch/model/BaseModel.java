package org.shredzone.repowatch.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseModel {
    protected long id;

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    public long getId()                     { return id; }
    public void setId(long id)              { this.id = id; }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BaseModel)) return false;
        return ((BaseModel) obj).getId() == this.getId();
    }
    
    public int hashCode() {
        return (int) ((getId()>>32) ^ getId());
    }

}
