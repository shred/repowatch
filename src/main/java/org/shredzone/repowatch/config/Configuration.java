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

package org.shredzone.repowatch.config;

/**
 * Implementations of this interface return basic configuration settings.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 317 $
 */
public interface Configuration {

    /**
     * The number of entries to be shown per page of a browser list.
     * 
     * @return Value
     */
    public int getEntriesPerPage();
    
    /**
     * The maximum number of days to look back in a RSS feed.
     * 
     * @return Value
     */
    public int getRssMaxNumberOfDays();
    
    /**
     * The maximum number of entries in a RSS feed.
     * 
     * @return Value
     */
    public int getRssMaxNumberOfEntries();
    
    /**
     * The minimum number of characters in a valid search term.
     * 
     * @return Value
     */
    public int getSearchTermMinLength();

}
