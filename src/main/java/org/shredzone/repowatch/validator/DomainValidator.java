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

package org.shredzone.repowatch.validator;

import org.shredzone.repowatch.model.Domain;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates a {@link Domain} entity.
 *
 * @author Richard "Shred" Körber
 * @version $Revision: 326 $
 */
public class DomainValidator implements Validator {

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Class clazz) {
        return Domain.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "validator.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "release", "validator.required");
    }

}
