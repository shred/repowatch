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

package org.shredzone.repowatch.web.util;

/**
 * A helper class that returns an endless sequence of the given item set.
 * This is useful in JSPs, e.g. for rendering each row of a table in a
 * different color.
 * <p>
 * Example:
 * <pre>
 * &lt;% pageContext.setAttribute("sequence", new Sequencer("oddrow", "evenrow")); %>
 * &lt;c:forEach var="entry" items="${entryList}">
 *   &lt;tr class="${sequence.next}">
 *     &lt;td>&lt;c:out value="${entry.name}"/>&lt;/td>
 *   &lt;/tr>
 * &lt;/c:forEach>
 * </pre>
 *
 * @author Richard "Shred" Körber
 */
public class Sequencer {
    private int pos = 0;
    private final String[] sequence;

    /**
     * Creates a new Sequencer with the given sequence.
     *
     * @param sequence  The sequence of strings to be used. Must have at least
     *      one entry.
     */
    public Sequencer(String... sequence) {
        if (sequence.length == 0)
            throw new IllegalArgumentException("At least one item is required!");

        this.sequence = sequence;
        this.pos = 0;
    }

    /**
     * Gets the next entry of the sequence. If the last entry was returned,
     * it will start again with the first entry.
     *
     * @return  Sequence entry
     */
    public String getNext() {
        if (pos >= sequence.length) pos = 0;
        return sequence[pos++];
    }

}
