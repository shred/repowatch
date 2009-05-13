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

package org.shredzone.repowatch.sync;

import java.util.Date;

/**
 * Contains all information about a repository database and it's location.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public class DatabaseLocation {

    private String type;
    private String location;
    private String checksum;
    private String checksumType;
    private long timestamp;
    private boolean compressed;

    /**
     * Gets the database type ("primary", "filelists", "other", ...)
     * 
     * @return Database type
     */
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    /**
     * Gets the database location. This is a relative URL to the repository
     * base URL (e.g. "repodata/primary.xml.gz").
     * 
     * @return Database location
     */
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    /**
     * Gets the checksum of the database.
     * 
     * @return Checksum
     */
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    /**
     * Gets the checksum type ("sha")
     * 
     * @return Checksum type
     */
    public String getChecksumType() { return checksumType; }
    public void setChecksumType(String checksumType) { this.checksumType = checksumType; }

    /**
     * Gets the timestamp of the last modification (milliseconds since epoch).
     * 
     * @return Timestamp
     */
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    /**
     * Gets a {@link Date} representation of {@link #getTimestamp()}.
     * 
     * @return Timestamp as {@link Date}
     */
    public Date getTimestampAsDate() {
        return new Date(getTimestamp());
    }

    /**
     * Returns <code>true</code> if the database XML file is gz compressed,
     * false if it's plaintext.
     * 
     * @return <code>true</code>: gz compressed database
     */
    public boolean isCompressed() { return compressed; }
    public void setCompressed(boolean compressed) { this.compressed = compressed; }
    
}
