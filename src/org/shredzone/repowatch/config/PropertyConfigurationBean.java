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
 * $Id: PropertyConfigurationBean.java 187 2008-07-27 13:40:08Z shred $
 */

package org.shredzone.repowatch.config;

/**
 * This implementation of the {@link Configuration} interface returns
 * sensible default values which can be changed by the setter methods.
 * Use this to configure the Configuration values in Spring's
 * application context.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 187 $
 */
public class PropertyConfigurationBean implements Configuration {
    
    private int entriesPerPage = 25;
    private int rssMaxNumberOfDays = 5;
    private int rssMaxNumberOfEntries = 50;
    private int searchTermMinLength = 3;

    /**
     * Sets the number of entries to be shown per page of a browser list.
     * 
     * @param value     Value
     */
    public void setEntriesPerPage(int value) {
        this.entriesPerPage = value;
    }
    
    /**
     * Sets the maximum number of days to look back in a RSS feed.
     * 
     * @param value     Value
     */
    public void setRssMaxNumberOfDays(int value) {
        this.rssMaxNumberOfDays = value;
    }
    
    /**
     * Sets the maximum number of entries in a RSS feed.
     * 
     * @param value     Value
     */
    public void setRssMaxNumberOfEntries(int value) {
        this.rssMaxNumberOfEntries = value;
    }
    
    /**
     * Sets the minimum number of characters in a valid search term.
     * 
     * @param value     Value
     */
    public void setSearchTermMinLength(int value) {
        this.searchTermMinLength = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEntriesPerPage() {
        return entriesPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRssMaxNumberOfDays() {
        return rssMaxNumberOfDays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRssMaxNumberOfEntries() {
        return rssMaxNumberOfEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSearchTermMinLength() {
        return searchTermMinLength;
    }

}
