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

package org.shredzone.repowatch.job;

import java.io.Serializable;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AbstractAuthenticationToken;

/**
 * An {@link Authentication} that allows a cron job to invoke methods that are secured
 * for "ROLE_CRON".
 *
 * @author Richard "Shred" Körber
 */
public final class CronAuthenticationToken extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = -3081468569286324821L;

    private final static GrantedAuthority[] ROLES = {
        new GrantedAuthorityImpl("ROLE_CRON"),
    };

    private final Object principal;

    /**
     * Creates a new {@link CronAuthenticationToken} for the given cron principal.
     *
     * @param principal
     *        The principal for this token. This is usually a reference to the
     *        cron job instance. {@code null} is not allowed.
     */
    public CronAuthenticationToken(Object principal) {
        super(ROLES);
        if (principal == null) {
            throw new IllegalArgumentException("principal must not be null");
        }
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

}

