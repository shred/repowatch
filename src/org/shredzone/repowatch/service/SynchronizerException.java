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
 * $Id: SynchronizerException.java 182 2008-07-23 13:57:39Z shred $
 */

package org.shredzone.repowatch.service;

/**
 * Shows that something went wrong while synchronizing.
 * 
 * @author Richard "Shred" Körber
 * @version $Revision: 182 $
 */
public class SynchronizerException extends Exception {
    private static final long serialVersionUID = -6701318058608586031L;

    public SynchronizerException() {
        super();
    }
    
    public SynchronizerException(String msg) {
        super(msg);
    }
    
    public SynchronizerException(String msg, Throwable cause) {
        super(msg,cause);
    }
    
}
